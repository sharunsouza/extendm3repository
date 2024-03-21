/**
*  Business Engine Extension
*/
/****************************************************************************************
Extension Name: EXT100MI.GetRecord
Type : ExtendM3Transaction
Script Author: GuruChetan Marthala
Date: 20240319

Description:
transaction to Get Records from EXTIBL tabel

Revision History:
Name                           Date          Version         Description of Changes
GuruChetan Marthala            20240319         1.0             Initial Version
******************************************************************************************/

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;

public class GetRecord extends ExtendM3Transaction {
  private final MIAPI mi;
  private final ProgramAPI program;
  private final DatabaseAPI database;

  private boolean foundRecord, nextRead;
  public GetRecord(MIAPI mi, ProgramAPI program, DatabaseAPI database) {
    this.mi = mi;
    this.program = program;
    this.database = database;
  }
  public void main() {
    // - Get CONO and input values
   
    int inCONO =  (mi.in.get("CONO") == null)? (Integer)program.getLDAZD().CONO : (int)mi.in.get("CONO")
    long inINBN = (long)mi.in.get("INBN")
    String inDIVI = (mi.in.get("DIVI") == null)? "": mi.in.get("DIVI")
    int inTRNO = (int)mi.in.get("TRNO")

    
	  DBAction dBquery = database.table("EXTIBL").index("00").selectAllFields().build();
    DBContainer EXT100 = dBquery.createContainer();
    EXT100.set("EXCONO", inCONO);
    EXT100.set("EXDIVI", inDIVI);
    EXT100.set("EXINBN", inINBN);
    EXT100.set("EXTRNO", inTRNO); 
    
    if (!dBquery.read(EXT100)){
      mi.error("Record not found");
    }else{
        mi.outData.put("PUNO", EXT100.get("EXPUNO").toString().trim());
        mi.outData.put("PNLI", EXT100.get("EXPNLI").toString().trim());
        mi.outData.put("ITNO", EXT100.get("EXITNO").toString().trim());
        mi.outData.put("SINO", EXT100.get("EXSINO").toString().trim());
        mi.outData.put("IVQA", EXT100.get("EXIVQA").toString().trim());
        mi.outData.put("NLAM", EXT100.get("EXNLAM").toString().trim());
        mi.outData.put("SSAM", EXT100.get("EXSSAM").toString().trim());
        mi.write();
    }
  }
}