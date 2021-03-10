import jxl.Sheet;
import jxl.Workbook;

import java.io.File;

public class UPLOAD_GOODS {
    public static void main(String[] args){
        try {
            File file = new File("C://Users/ASUS/Desktop/good.xls");
            DatabaseHelper databaseHelper = new DatabaseHelper();
            databaseHelper.init();
            databaseHelper.ChooseDatabase("SHOP");
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet=workbook.getSheet(0);
            int times=sheet.getRows();
            int x=0;
            for(int i=0;i<sheet.getRows();i++){
                System.out.println((++x)+"/"+times);
                databaseHelper.ExecuteSQL("INSERT INTO GOODS(ID,NAME,SHOPGROUP,LINK) VALUES("+Integer.parseInt(sheet.getCell(0,i).getContents())+
                        ",\'"+sheet.getCell(1,i).getContents().replace("'","")+"\'," +Integer.parseInt(sheet.getCell(2,i).getContents().replace("'",""))+",\'"+sheet.getCell(3,i).getContents().replace("'","")+"\');");
            }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Nihao!!!");
        }
    }
}
