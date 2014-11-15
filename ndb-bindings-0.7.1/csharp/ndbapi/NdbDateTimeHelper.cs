// NdbDateTime.cs created with MonoDevelop
// User: mtaylor at 9:53 AM 1/3/2008
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//

using System;
using System.Runtime.InteropServices;

namespace MySql.Cluster.NdbApi
{
	
	internal class NdbDateTimeHelper { 
	  public static HandleRef systemDTtoNdbDT(System.DateTime theDateTime) { 
			NdbDateTime dt = new NdbDateTime();
			dt.year=(uint)theDateTime.Year;
			dt.month=(uint)theDateTime.Month;
			dt.day=(uint)theDateTime.Day;
			dt.hour=(uint)theDateTime.Hour;
			dt.minute=(uint)theDateTime.Minute;
			dt.second=(uint)theDateTime.Second;
			return NdbDateTime.getCPtr(dt);
			                                
		}
		
	}
	
	[StructLayout (LayoutKind.Sequential)]
	internal struct mtNdbDateTime
	{
		public uint year;
		public uint month; 
		public uint day;
		public uint hour;
		public uint minute;
		public uint second;
	}

}
