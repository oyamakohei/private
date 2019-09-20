package openApiList_Tool;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChooser {
	public File main(String fileExtension) {
		
		boolean ymlfileChoose = false;
        System.out.println(ymlfileChoose);
		File file = null;
		
		while(!ymlfileChoose) {
	        JFileChooser chooser = new JFileChooser();
	        //デフォルトの選択ファイルを指定
	        String cd = new File(".").getAbsoluteFile().getParent();
	        chooser.setSelectedFile(new File(cd));
	        //ダイアログを表示
	        chooser.showOpenDialog(null);
	        //ダイアログの選択結果を取得
	        file = chooser.getSelectedFile();
	        
	        if(file != null && file.toString().indexOf("."+fileExtension) != -1) {
	            System.out.println("選択したファイルを含んだフルパス:" + file.getPath());
	            System.out.println("選択したファイルのあるディレクトリ（フォルダ）:" + file.getParent());
	            System.out.println("選択したファイルのファイル名:" + file.getName());
	            ymlfileChoose = true; 
	        }else {
	            System.out.println(fileExtension + "ファイルを選択してください");

	            JOptionPane.showMessageDialog(null, fileExtension + "ファイルを選択してください");
	        	continue;	            
	        }
	    }
        return file;
	}
}