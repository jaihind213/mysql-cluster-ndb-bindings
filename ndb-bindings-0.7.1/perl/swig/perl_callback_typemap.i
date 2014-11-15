%typemap(in) asynch_callback_t * {   

  $1 = new asynch_callback_t;
  $1->obj = $input;
  $1->create_time = 0;
  SvREFCNT_inc($input);
 }


%{
/* This function matches the prototype of the normal C callback
   function for our widget. However, we use the clientdata pointer
   for holding a reference to a Perl callable object. */


typedef struct
asynch_callback_t
{  
  SV * obj;
  long long create_time;
};

static void theCallBack(int result,
		     NdbTransaction *trans,
		     void *cbData)
{
  asynch_callback_t * callback_data = (asynch_callback_t *)cbData;
  SV * perlFunc = callback_data->obj;
  dSP;
  ENTER;
  SAVETMPS;
  PUSHMARK(sp);
  SV* pSV = sv_newmortal();
  sv_setiv(pSV, result);
  XPUSHs(pSV);
  SV* trans_obj = SWIG_NewPointerObj(SWIG_as_voidptr(trans), SWIGTYPE_p_NdbTransaction, 0 |  0 );
  XPUSHs(trans_obj);
  PUTBACK;
  perl_call_sv( perlFunc, G_VOID);
  FREETMPS;
  SvREFCNT_dec( perlFunc );
  delete callback_data;
  LEAVE;
}

%}

