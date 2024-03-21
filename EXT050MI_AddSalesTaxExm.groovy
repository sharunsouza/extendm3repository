import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.lang.String;

public class AddSalseTaxExm extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final UtilityAPI utility;
  private final ProgramAPI program;
  
   //Input fields
  private String inCONO;
  private String inDIVI;
  private String inPRIO;
  private String inOBV1;
  private String inOBV2;
  private String inOBV3;
  private String inOBV4;
  private String inFRDT;
  private String inTODT;
  private String inRTXC;
  private String inTYPE;

  public AddSalseTaxExm(MIAPI mi, DatabaseAPI database,UtilityAPI utility, ProgramAPI program) {
    this.mi = mi;
    this.database = database;
    this.utility = utility;
    this.program = program;
  }
  
  public void main() {
    
       
         if(mi.in.get("CONO") == null ||(mi.inData.get("CONO")).isEmpty()){
           inCONO = "0";
         } else {
           inCONO = mi.inData.get("CONO").trim();
          }
       if(mi.in.get("TYPE") == null ||(mi.inData.get("TYPE")).isEmpty()){
           inTYPE = "0";
         } else {
           inTYPE = mi.inData.get("TYPE").trim();
          }
      
     
       if(mi.in.get("PRIO") == null ||(mi.inData.get("PRIO")).isEmpty()){
           inPRIO = "0";
         } else {
           inPRIO = mi.inData.get("PRIO").trim();
          }
      inDIVI =(mi.in.get("DIVI") == null || mi.in.get("DIVI") == 'null')? "": mi.in.get("DIVI");
      inOBV1 =(mi.in.get("OBV1") == null || mi.in.get("OBV1") == 'null')? "": mi.in.get("OBV1");
      inOBV2 =(mi.in.get("OBV2") == null || mi.in.get("OBV2") == 'null')? "": mi.in.get("OBV2");
      inOBV3 =(mi.in.get("OBV3") == null || mi.in.get("OBV3") == 'null')? "": mi.in.get("OBV3");
      inOBV4 =(mi.in.get("OBV4") == null || mi.in.get("OBV4") == 'null')? "": mi.in.get("OBV4");
      inRTXC =(mi.in.get("RTXC") == null || mi.in.get("RTXC") == 'null')? "": mi.in.get("RTXC");
     // inFRDT =(mi.in.get("FRDT") == null || mi.in.get("FRDT") == 'null')? "": mi.in.get("FRDT");
     // inTODT =(mi.in.get("TODT") == null || mi.in.get("TODT") == 'null')? "": mi.in.get("TODT");
    
    
     if(mi.in.get("FRDT") == null ||(mi.inData.get("FRDT")).isEmpty()){
           inFRDT = "0";
         } else {
           inFRDT = mi.inData.get("FRDT").trim();
          }
           if(mi.in.get("TODT") == null ||(mi.inData.get("TODT")).isEmpty()){
           inTODT = "0";
         } else {
           inTODT = mi.inData.get("TODT").trim();
          }
      
     DBAction SYSTM = database.table("CSYTAB").index("00").selection("CTCONO","CTDIVI","CTSTCO","CTSTKY","CTLNCD","CTTX40").build()
     DBContainer conSYSTM = SYSTM.getContainer()

    conSYSTM.set("CTCONO", Integer.parseInt(inCONO))
    conSYSTM.set("CTSTCO", "TAXC")
    conSYSTM.set("CTSTKY", inRTXC)
    if (SYSTM.read(conSYSTM)) {
       
        
        String taxcode =conSYSTM.get("CTSTKY").toString().trim();
       
          if (taxcode.equals(inRTXC)){
            
             addSalesTaxExem();
          }else
          {
            mi.error("Invaild Tax Code"+"_"+taxcode);
          }
     
    }  else  {
        mi.error("Tax Code record does not exist");
        return;
    }
   
  }
  /**
	 * Add Sales Tax Exemption
	 * 
	 * @param 
	 * @return 
	*/
  
  void addSalesTaxExem() {
	 
	  //Check Region Details.
    DBAction dbaEXT050 = database.table("FTXTXC").index("00").selection("TCCONO","TCDIVI","TCTYPE","TCPRIO","TCOBV1","TCOBV2","TCOBV3","TCOBV4","TCFRDT","TCTODT","TCRTXC").build()
    DBContainer conEXT050 = dbaEXT050.getContainer()
 
    conEXT050.set("TCCONO", Integer.parseInt(inCONO))
    conEXT050.set("TCDIVI", inDIVI)
    conEXT050.set("TCTYPE", Integer.parseInt(inTYPE))
    conEXT050.set("TCPRIO", Integer.parseInt(inPRIO))
    conEXT050.set("TCOBV1", inOBV1)
    conEXT050.set("TCOBV2", inOBV2)
    conEXT050.set("TCOBV3", inOBV3)
    conEXT050.set("TCOBV4", inOBV4)
    conEXT050.set("TCFRDT", inFRDT.toInteger())
    conEXT050.set("TCTODT", inTODT.toInteger())
    conEXT050.set("TCRTXC", inRTXC)
	  if(dbaEXT050.read(conEXT050)) {
      mi.error("Record already Exist");
    
      return;
    } else {
      
    conEXT050.set("TCCONO", Integer.parseInt(inCONO))
    conEXT050.set("TCDIVI", inDIVI)
    conEXT050.set("TCTYPE", Integer.parseInt(inTYPE))
    conEXT050.set("TCPRIO", Integer.parseInt(inPRIO))
    conEXT050.set("TCOBV1", inOBV1)
    conEXT050.set("TCOBV2", inOBV2)
    conEXT050.set("TCOBV3", inOBV3)
    conEXT050.set("TCOBV4", inOBV4)
    conEXT050.set("TCFRDT", inFRDT.toInteger())
    conEXT050.set("TCTODT", inTODT.toInteger())
    conEXT050.set("TCRTXC", inRTXC)
    conEXT050.set("TCTXID", 0)
    conEXT050.set("TCRGDT", 0)
    conEXT050.set("TCRGTM", 0)
    conEXT050.set("TCLMDT", 0)
    conEXT050.set("TCCHNO", 0)
    conEXT050.set("TCLMTS", 0.0)
    conEXT050.set("TCRLAM", 0)
    
      Closure<?> insertCallBack = {
          
      };
      dbaEXT050.insert(conEXT050, insertCallBack);
    }
   mi.outData.put("TYPE", conEXT050.get("TCTYPE").toString());   
   mi.outData.put("PRIO", conEXT050.get("TCPRIO").toString());   
   mi.outData.put("OBV1", conEXT050.get("TCOBV1").toString());   
   mi.outData.put("OBV2", conEXT050.get("TCOBV2").toString());   
   mi.outData.put("OBV3", conEXT050.get("TCOBV3").toString());   
   mi.outData.put("FRDT", conEXT050.get("TCFRDT").toString());  
   mi.outData.put("RTXC", conEXT050.get("TCRTXC").toString()); 
   mi.outData.put("TODT", conEXT050.get("TCTODT").toString()); 
 
  }
  
  /**
	 * Validate inputs along with null checks 
	 * 
	 * @param 
	 * @return boolean errror
	*/
	
 
}  