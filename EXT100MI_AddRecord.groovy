/**
*  Business Engine Extension
*/
/****************************************************************************************
Extension Name: EXT100MI.AddRecord
Type : ExtendM3Transaction
Script Author: GuruChetan Marthala
Date: 20240319

Description:
transaction to Add Records in EXTIBL tabel

Revision History:
Name                           Date          Version         Description of Changes
GuruChetan Marthala            20240319         1.0             Initial Version
******************************************************************************************/

public class AddRecord extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final ProgramAPI program;
  private final UtilityAPI utility;
  public AddRecord(MIAPI mi,DatabaseAPI database, ProgramAPI program, UtilityAPI utility) {
    this.mi = mi
    this.database = database
    this.program = program
    this.utility = utility
  }
  public void main() {
     int CONO =  (mi.in.get("CONO") == null)? (Integer)program.getLDAZD().CONO : (int)mi.in.get("CONO")
     long INBN = (long)mi.in.get("INBN")
     String DIVI = (mi.in.get("DIVI") == null)? "": mi.in.get("DIVI")
     String PUNO = (mi.in.get("PUNO") == null)? "": mi.in.get("PUNO")
     int PNLI = (mi.in.get("PNLI") == null)? 0:(int) mi.in.get("PNLI")
     String ITNO = (mi.in.get("ITNO") == null)? "": mi.in.get("ITNO")
     String SINO = (mi.in.get("SINO") == null)? "": mi.in.get("SINO")
     double IVQA = (mi.in.get("IVQA") == null)? 0: (int)mi.in.get("IVQA")
     double NLAM = (mi.in.get("NLAM") == null)? 0: (double)mi.in.get("NLAM")
     double SSAM = (mi.in.get("SSAM") == null)? 0: (double)mi.in.get("SSAM")
     int TRNO = (int)mi.in.get("TRNO")
    
    DBAction dbaCMNDIV = database.table("CMNDIV").index("00").build();
    DBContainer conCMNDIV = dbaCMNDIV.getContainer();
    conCMNDIV.set("CCCONO", CONO);
    conCMNDIV.set("CCDIVI", DIVI);

    if (!dbaCMNDIV.read(conCMNDIV)) {
      mi.error("Record does not exist CMNDIV");
      return;
    }
    
    ExpressionFactory expression = database.getExpressionFactory("FAPIBH");
    expression = expression.eq("E5SINO", SINO.trim());
    DBAction dbaFAPIBH = database.table("FAPIBH").index("00").matching(expression).build();
    DBContainer conFAPIBH = dbaFAPIBH.getContainer();
    conFAPIBH.set("E5CONO", CONO);
    conFAPIBH.set("E5DIVI", DIVI);
    conFAPIBH.set("E5INBN", INBN.toLong());


    if (!dbaFAPIBH.read(conFAPIBH)) {
      mi.error("Record does not exist FAPIBH");
      return;
    }
    
    DBAction dbaFAPIBL = database.table("FAPIBL").index("00").build();
    DBContainer conFAPIBL = dbaFAPIBL.getContainer();
    conFAPIBL.set("E6CONO", CONO);
    conFAPIBL.set("E6DIVI", DIVI);
    conFAPIBL.set("E6INBN", INBN.toLong());
	  conFAPIBL.set("E6TRNO", TRNO.toInteger());

    if (!dbaFAPIBL.read(conFAPIBL)) {
      mi.error("Record does not exist FAPIBL");
      return;
    }
    
    DBAction dbaMITMAS = database.table("MITMAS").index("00").build();
    DBContainer conMITMAS = dbaMITMAS.getContainer();
    conMITMAS.set("MMCONO", CONO);
    conMITMAS.set("MMITNO", ITNO);
    if (!dbaMITMAS.read(conMITMAS)) {
      mi.error("Record does not exist MITMAS");
      return;
    }
    
    DBAction dbaMPLINE = database.table("MPLINE").index("00").build();
    DBContainer conMPLINE = dbaMPLINE.getContainer();
    conMPLINE.set("IBCONO", CONO);
    conMPLINE.set("IBPUNO", PUNO);
  	conMPLINE.set("IBPNLI", PNLI.toInteger());
    if (!dbaMPLINE.read(conMPLINE)) {
      mi.error("Record does not exist MPLINE");
      return;
    }
     DBAction queryEXT100 = database.table("EXTIBL").index("00").build()
     DBContainer addRecordEXT100 = queryEXT100.getContainer()
     addRecordEXT100.set("EXCONO",CONO)
     addRecordEXT100.set("EXDIVI",DIVI)
     addRecordEXT100.set("EXINBN",INBN)
     addRecordEXT100.set("EXTRNO",TRNO)
    if(queryEXT100.read(addRecordEXT100))
    {
      mi.error("Record already exists")
    }
    else
    {
      int currentDate = utility.call("DateUtil", "currentDateY8AsInt")
      int currentTime = utility.call("DateUtil", "currentTimeAsInt")
    
      addRecordEXT100.set("EXSINO",SINO)
      addRecordEXT100.set("EXPUNO",PUNO)
      addRecordEXT100.set("EXPNLI",PNLI)
      addRecordEXT100.set("EXITNO",ITNO)
      addRecordEXT100.set("EXIVQA",IVQA)
      addRecordEXT100.set("EXNLAM",NLAM)
      addRecordEXT100.set("EXSSAM",SSAM)
      addRecordEXT100.set("EXRGDT",currentDate)
      addRecordEXT100.set("EXRGTM",currentTime)
      addRecordEXT100.set("EXCHID",program.getUser())
      addRecordEXT100.set("EXCHNO",1)
 
      queryEXT100.insert(addRecordEXT100);
    }

  }
}