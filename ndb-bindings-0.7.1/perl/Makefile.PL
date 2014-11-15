#!/usr/bin/env perl

use ExtUtils::MakeMaker;

$CFLAGS=$ENV{CFLAGS} || "";
$LDFLAGS=$ENV{LDFLAGS} || ""; 
$CC=$ENV{CC} || "gcc";


WriteMakefile(
    'DISTNAME'	=> "ndbapi",
    'NAME'	=> "ndbapi",
    'AUTHOR'    => 'Monty Taylor <mtaylor@mysql.com>',
    'ABSTRACT'  => "NDB/Perl",

    'DEFINE'    => "-DHAVE_CONFIG_H $CFLAGS",
#    'OBJECT'    => "ndbapi_wrap.o mgmapi_wrap.o",
#    'dynamic_lib' => {'OTHERLDFLAGS' => "$CFLAGS $LDFLAGS -L/usr/local/mysql/lib -lstdc++ -lndbclient -lmysqlclient -lmysys"},

#    'PM' =>  { map { ("$_", "\$(INST_LIBDIR)/$_") }
#	qw(lib/mysqlndbapi.pm mgmapi.pm)     
#	},

    'MAKEFILE'  => 'Makefile.perl',
    'clean'     => { FILES => "test*.db" },
#    'realclean' => { FILES => "${CORE_PACKAGE}_wrap.cpp ${CORE_PACKAGE}_wrap.o ndbapi.pm" },
);
