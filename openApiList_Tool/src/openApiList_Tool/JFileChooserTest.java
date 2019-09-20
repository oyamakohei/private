package openApiList_Tool;
import java.io.File;
import javax.swing.JFileChooser;

public class JFileChooserTest {
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		//デフォルトの選択ファイルを指定
		chooser.setSelectedFile(new File("C:\\test\\test1\\a.txt"));
		//ダイアログを表示
		chooser.showOpenDialog(null);
		//ダイアログの選択結果を取得
		File file = chooser.getSelectedFile();
		if(file != null) {
			System.out.println("選択したファイルを含んだフルパス:" + file.getPath());
			System.out.println("選択したファイルのあるディレクトリ（フォルダ）:" + file.getParent());
			System.out.println("選択したファイルのファイル名:" + file.getName());
		}else {
			System.out.println("選択してくれなかったよ(TT)");
		}
	}
}