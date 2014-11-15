#!/usr/bin/env perl

use ExtUtils::MakeMaker;

$CFLAGS=$ENV{CFLAGS} || "";
$LDFLAGS=$ENV{LDFLAGS} || ""; 


WriteMakefile(
    'NAME'	=> "ndbapi",
    'AUTHOR'    => 'Monty Taylor <mtaylor@mysql.com>',
    'ABSTRACT'  => "NDB/Perl",

    'DEFINE'    => "-DHAVE_CONFIG_H $CFLAGS",
    'OBJECT'    => "ndbapi_wrap.o",
    'dynamic_lib' => {'OTHERLDFLAGS' => "$CFLAGS $LDFLAGS -lstdc++"},

    'MAKEFILE'  => 'Makefile.perl',
    'clean' => { FILES => "ndbapi_wrap.cpp ndbapi_wrap.o ndbapi.bs" },
);
