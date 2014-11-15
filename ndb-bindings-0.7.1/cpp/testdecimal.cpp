#include <stdio.h>
#include <malloc.h>
#include <math.h>
#include <ndbapi/NdbApi.hpp>


#define DECIMAL_BUFF 9

  typedef signed int int32;
  typedef int32 decimal_digit_t;

  typedef struct st_decimal_t {
    int intg, frac, len;
    bool sign;
    decimal_digit_t *buf;
  } decimal_t;


#define string2decimal(A,B,C) internal_str2dec((A), (B), (C), 0)
#define decimal_string_size(dec) (((dec)->intg ? (dec)->intg : 1) +     \
                                  (dec)->frac + ((dec)->frac > 0) + 2)
  extern "C" {
    int decimal_size(int precision, int scale);
    int decimal_bin_size(int precision, int scale);
    int decimal2bin(decimal_t *from, char *to, int precision, int scale);
    int bin2decimal(char *from, decimal_t *to, int precision, int scale);
    int decimal2string(decimal_t *from, char *to, int *to_len,
                       int fixed_precision, int fixed_decimals,
                       char filler);
    int internal_str2dec(const char *from, decimal_t *to, char **end,
                         bool fixed);
  }

int main() { 

  ndb_init();
  Ndb_cluster_connection * conn = new Ndb_cluster_connection("127.0.0.1");
  conn->connect(5,3,1);
  conn->wait_until_ready(5,5);
  Ndb * ndb = new Ndb(conn, "test");
  ndb->init();
  const NdbDictionary::Dictionary* dict= ndb->getDictionary();
  const NdbDictionary::Table *t_decimal= dict->getTable("t_decimal");
  const NdbDictionary::Column * val_col = t_decimal->getColumn("val"); 
  NdbTransaction *myTransaction= ndb->startTransaction();
  NdbOperation *myOperation= myTransaction->getNdbOperation(t_decimal);
  myOperation->readTuple();
  myOperation->equal("id",1);
  NdbRecAttr * self = myOperation->getValue(val_col);

  printf("NdbRecAttr<%p>\n",self);
  myTransaction->execute( NdbTransaction::Commit);

  printf("NdbRecAttr<%p>\n",self);

  decimal_t * dec = NULL; 

  int prec = self->getColumn()->getPrecision(); 
  printf("prec:%d\n",prec);
  int scale = self->getColumn()->getScale();
  printf("scale:%d\n",scale);

  char * ref = self->aRef();
  printf("ref<%p>\n",ref);


  dec = (decimal_t *)malloc(sizeof(decimal_t));
  dec->intg = 0;
  dec->frac = 0;
  dec->len = ceil((prec/8.0))+3;
  dec->sign = 0;
  dec->buf = (decimal_digit_t *)malloc(sizeof(decimal_digit_t)*(dec->len));
  printf("prec%d scale%d\n",prec,scale);
  printf("ref<%p>\n",ref);

  int ret = bin2decimal(ref, dec, prec, scale);
  printf("ref<%p>\n",ref);
  printf("theValue from getDecimal:%d,%d\n",ret,dec->buf[0]);
  printf("intg:%d frac:%d len:%d\n",dec->intg,dec->frac,dec->len);
  printf("%d : %d %d %d\n",ret,dec->buf[0], dec->buf[1],  dec->buf[2]);
  
  delete ndb; 
  delete conn;
  return 0;
}
