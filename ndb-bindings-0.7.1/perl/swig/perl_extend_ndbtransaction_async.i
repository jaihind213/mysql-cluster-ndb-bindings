%extend NdbTransaction { 

%feature("shadow") executeAsynchPrepare(ExecType execType,
                            async_callback_t * cb,
                            AbortOption          abortOption = AbortOnError)
%{
    sub executeAsynchPrepare {
        my $self=shift;
        my $exec=shift;
        my $sub=shift;
        my $abort=shift; 


ndbapic::NdbTransaction_executeAsynchPrepare($self, $exec,
            sub { $sub->(); $self; }, $abort);
    }
%} 

};


