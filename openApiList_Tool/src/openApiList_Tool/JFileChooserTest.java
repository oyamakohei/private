package openApiList_Tool;
import java.io.File;
import javax.swing.JFileChooser;

public class JFileChooserTest {
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		//�f�t�H���g�̑I���t�@�C�����w��
		chooser.setSelectedFile(new File("C:\\test\\test1\\a.txt"));
		//�_�C�A���O��\��
		chooser.showOpenDialog(null);
		//�_�C�A���O�̑I�����ʂ��擾
		File file = chooser.getSelectedFile();
		if(file != null) {
			System.out.println("�I�������t�@�C�����܂񂾃t���p�X:" + file.getPath());
			System.out.println("�I�������t�@�C���̂���f�B���N�g���i�t�H���_�j:" + file.getParent());
			System.out.println("�I�������t�@�C���̃t�@�C����:" + file.getName());
		}else {
			System.out.println("�I�����Ă���Ȃ�������(TT)");
		}
	}
}