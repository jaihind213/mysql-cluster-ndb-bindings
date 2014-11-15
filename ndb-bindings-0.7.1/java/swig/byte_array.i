/* -*- mode: c++; c-basic-offset: 2; indent-tabs-mode: nil; -*-
 *  vim:expandtab:shiftwidth=2:tabstop=2:smarttab:
 *
 *  ndb-bindings: Bindings for the NDB API
 *  Copyright (C) 2008 MySQL
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

%typemap(jni) BYTES "jbyteArray"
%typemap(jtype) BYTES "byte[]"
%typemap(jstype) BYTES "byte[]"
%typemap(javaout) BYTES { return $jnicall; }


%typemap(out) (BYTES)  {

  $result = jenv->NewByteArray($1.theLength);
  jenv->SetByteArrayRegion($result,0,$1.theLength,(jbyte*) $1.theString);

 }

%typemap(in) (const char* BYTE, size_t len) {
  $1 = (char *)(jenv->GetByteArrayElements($input,0));
  $2 = jenv->GetArrayLength($input);
 }
