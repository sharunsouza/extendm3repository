/**
*  Business Engine Extension
*/
/****************************************************************************************
Extension Name: EXT100MI.DeleteRecord
Type : ExtendM3Transaction
Script Author: GuruChetan Marthala
Date: 20240319

Description:
transaction to Delete Records from EXTIBL tabel

Revision History:
Name                           Date          Version         Description of Changes
GuruChetan Marthala            20240319         1.0             Initial Version
******************************************************************************************/

public class DeleteRecord extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final ProgramAPI program;
  public DeleteRecord(MIAPI mi,DatabaseAPI database, ProgramAPI program) {
    this.mi = mi;
    this.database = database
    this.program = program
  }

  public void main() {

    int inCONO =  (mi.in.get("CONO") == null)? (Integer)program.getLDAZD().CONO : (int)mi.in.get("CONO")
    long inINBN = (long)mi.in.get("INBN")
    String inDIVI = (mi.in.get("DIVI") == null)? "": mi.in.get("DIVI")
    int inTRNO = (int)mi.in.get("TRNO")
	  DBAction dBquery = database.table("EXTIBL").index("00").build();
    DBContainer EXT100 = dBquery.createContainer();
    EXT100.set("EXCONO", inCONO);
    EXT100.set("EXDIVI", inDIVI);
    EXT100.set("EXINBN", inINBN);
    EXT100.set("EXTRNO", inTRNO); 
 
		  Closure<?> deleterCallback = { LockedResult lockedResult ->
      lockedResult.delete()
    }
    if (!dBquery.readLock(EXT100, deleterCallback)) {
		  mi.error("Record does not exist");
		  return;
	  }
		}
}