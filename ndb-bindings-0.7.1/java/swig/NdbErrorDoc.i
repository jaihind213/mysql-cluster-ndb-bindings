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

%typemap(javaimports) NdbError %{
/**
 * @class NdbError
 * @brief Contains error information
 *
 * A NdbError consists of five parts:
 * -# Error status         : Application impact
 * -# Error classification : Logical error group
 * -# Error code           : Internal error code
 * -# Error message        : Context independent description of error
 * -# Error details        : Context dependent information
 *                           (not always available)
 *
 * <em>Error status</em> is usually used for programming against errors.
 * If more detailed error control is needed, it is possible to
 * use the <em>error classification</em>.
 *
 * It is not recommended to write application programs dependent on
 * specific <em>error codes</em>.
 *
 * The <em>error messages</em> and <em>error details</em> may
 * change without notice.
 *
 * It is highly unlikely the user will want to use this class from Java.
 * The use of thrown exceptions is preferrable.
 */
  %}
