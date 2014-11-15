%{
#include <my_time.h>

void factor_HHMMSS(MYSQL_TIME *tm, int int_time) {
  if(int_time < 0) {
    tm->neg = true; int_time = - int_time;
  }
  tm->hour = int_time/10000;
  tm->minute  = int_time/100 % 100;
  tm->second  = int_time % 100;  
}

void factor_YYYYMMDD(MYSQL_TIME *tm, int int_date) {
  tm->year = int_date/10000 % 10000;
  tm->month  = int_date/100 % 100;
  tm->day = int_date % 100;  
}

void field_to_tm(MYSQL_TIME *tm, const NdbRecAttr &rec) {
  int int_date = -1, int_time = -99;
  Uint64 datetime;

  bzero (tm, sizeof(MYSQL_TIME));
  switch(rec.getType()) {
    case NdbDictionary::Column::Datetime :
      datetime = rec.u_64_value();
      int_date = datetime / 1000000;
      int_time = datetime - (unsigned long long) int_date * 1000000;
      break;
    case NdbDictionary::Column::Time :
    {  
      int_time = sint3korr(rec.aRef());
      break;
    }
    case NdbDictionary::Column::Date :
      int_date = uint3korr(rec.aRef());
      tm->day = (int_date & 31);      // five bits
      tm->month  = (int_date >> 5 & 15); // four bits
      tm->year = (int_date >> 9);
      return;
    default:
      assert(0);
  }
  if(int_time != -99)factor_HHMMSS(tm, int_time);
  if(int_date != -1) factor_YYYYMMDD(tm, int_date);
}


%}

%typemap(in, numinputs=0) const NdbRecAttr** OutValueProxy (NdbRecAttr* temp) {
    temp=0;
    $1=&temp;
}

%typemap(argout) const NdbRecAttr** OutValueProxy {
    NdbRecAttr* rec=*$1;
    if (rec->isNULL()==-1) {
        croak("record not fetched");
    }
    if (rec->isNULL()) {
        $result=newSV(0);
    } else {
        char dt_buf[20];
        MYSQL_TIME tm;
        switch(rec->getType()) {
            case NDB_TYPE_CHAR:
            case NDB_TYPE_BINARY:
	    {
	        $result=newSVpvn(rec->aRef(), rec->get_size_in_bytes());
	        break;
	    }
            case NDB_TYPE_VARCHAR:
            case NDB_TYPE_VARBINARY:
	    {
	        const char* buf=rec->aRef();
	        int len=buf[0];
	        buf++;
	        $result=newSVpvn(buf, len);
	        break;
	    }
            case NDB_TYPE_TINYINT:
	    {
                $result=newSViv(rec->char_value());
                break;
	    }
            case NDB_TYPE_SMALLINT:
	    {
                $result=newSViv(rec->short_value());
                break;
	    }
            case NDB_TYPE_MEDIUMINT:
	    {
                $result=newSViv(sint3korr(rec->aRef()));
                break;
	    }
            case NDB_TYPE_INT:
	    {
                $result=newSViv(rec->int32_value());
                break;
	    }
            case NDB_TYPE_TINYUNSIGNED:
	    {
                $result=newSVuv(rec->u_char_value());
                break;
	    }
            case NDB_TYPE_SMALLUNSIGNED:
	    {
                $result=newSVuv(rec->u_short_value());
                break;
	    }
            case NDB_TYPE_MEDIUMUNSIGNED:
	    {
                $result=newSVuv(uint3korr(rec->aRef()));
                break;
	    }
            case NDB_TYPE_UNSIGNED:
	    {
                $result=newSVuv(rec->u_32_value());
                break;
	    }
            case NDB_TYPE_DATETIME:
            case NDB_TYPE_DATE:
            case NDB_TYPE_TIME:
                field_to_tm(&tm, *rec);
                switch(rec->getType()) {
                    case NDB_TYPE_DATETIME:
                        snprintf(dt_buf, 20, "%04d-%02d-%02d %02d:%02d:%02d", tm.year, tm.month, 
                                      tm.day, tm.hour, tm.minute, tm.second);
                        break;
                    case NDB_TYPE_DATE:
                        snprintf(dt_buf, 20, "%04d-%02d-%02d",tm.year, tm.month, tm.day);
                        break;
                    case NDB_TYPE_TIME:
                        snprintf(dt_buf, 20, "%s%02d:%02d:%02d", tm.neg ? "-" : "" ,
                                      tm.hour, tm.minute, tm.second);
                        break;
                }
                $result=newSVpv(dt_buf, 0);
                break;
        }
    }
    argvi++;
}
