#!/usr/bin/env perl

use ExtUtils::MakeMaker;

$CFLAGS=$ENV{CFLAGS} || "";
$LDFLAGS=$ENV{LDFLAGS} || ""; 


WriteMakefile(
    'NAME'	=> "mgmapi",
    'AUTHOR'    => 'Monty Taylor <mtaylor@mysql.com>',
    'ABSTRACT'  => "NDB/Perl",

    'DEFINE'    => "-DHAVE_CONFIG_H $CFLAGS",
    'OBJECT'    => "mgmapi_wrap.o",
    'dynamic_lib' => {'OTHERLDFLAGS' => "$CFLAGS $LDFLAGS -lstdc++"},


    'MAKEFILE'  => 'Makefile.perl',
    'clean' => { FILES => "mgmapi_wrap.cpp mgmapi_wrap.o mgmapi.bs" },
);
