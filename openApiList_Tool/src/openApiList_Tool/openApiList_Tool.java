package openApiList_Tool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

public class openApiList_Tool {
	public static void main(String[] args) {
		final String YML = "yml";
		final String TXT = "txt";
		Path path = Paths.get("C:\\ooyama\\git\\swagger\\zipair-backend-for-frontend-swagger\\openapi.yml");

		FileChooser fc = new FileChooser();

		/*
		 * JOptionPane.showMessageDialog(null, "入力するファイルを選択してください"); path =
		 * fc.main(YML).toPath();
		 */

		// openapiファイルから対象ファイルを読み取る
		ArrayList<String> apiFile = new ArrayList<String>();
		apiFile = getfile(path);

		// 対象のファイルを配列に格納する
		ArrayList<Path> tagetFiles = new ArrayList<Path>();
		for (String file : apiFile) {
			file = file.replace("/", "\\");
			tagetFiles.add(FileSystems.getDefault().getPath(file.replace(".\\", path.getParent().toString() + "\\")));
		}

		String[][] arraydata;

		if (tagetFiles.size() != 0) {
			arraydata = setarray(tagetFiles);
		} else {
			arraydata = setarrayS(path);
		}

		String var;
		List<String> list = Arrays.stream(arraydata).map(line -> String.join(",", line)).collect(Collectors.toList());

		// OutPutしたい
		// 読み書きするファイルパス
		Path outpath = Paths.get("C:\\ooyama\\memo.csv");
		StringBuilder sb = new StringBuilder();
		/*
		 * JOptionPane.showMessageDialog(null, "出力先のファイルを選択してください");
		 * if(outpath.toFile().exists()){ outpath = fc.main(TXT).toPath(); }
		 */
		try (BufferedWriter writer = Files.newBufferedWriter(outpath)) {
			for (int i = 0; i < arraydata.length; i++) {
				sb.setLength(0);

				for (int t = 0; t < arraydata[0].length; t++) {
					var = arraydata[i][t];
					if (var == null) {
						// エラー回避
					} else if (!var.isEmpty()) {
						var = var.replaceAll("\n", "\r\n");
					}
					sb.append("\"" + var + "\",");
				}
				// sb = sb.deleteCharAt(sb.length()-1);
				writer.append(sb);
				if (apiFile.isEmpty()) {
					writer.append(path.toString());
				} else {
					writer.append(apiFile.get(i).toString());
				}
				writer.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String> getfile(Path file) {

		// 変数定義
		ArrayList<String> apiFile = new ArrayList<String>();

		final String YMLFILE_START = "$ref:";
		final String YMLFILE_END = ".yml";

		// fileOpen
		try {
			List<String> lines = Files.readAllLines((file), StandardCharsets.UTF_8);
			for (String s : lines) {
				int result = s.indexOf(YMLFILE_START);
				int result2 = s.indexOf(YMLFILE_END);
				if (result != -1 & result2 != -1) {
					apiFile.add(s.substring(s.indexOf("./"), result2 + 4));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return apiFile;
	}

	public static String[][] setarray_BackUp(ArrayList<Path> files) {

		final String SUMMARY = "summary:";
		final String TAGS = "tags:";
		final String DESCRIPTION = "description:";
		final String OPERATIONID = "operationId:";
		final String ESCAPE = "|-";

		final int OPERATIONID_NO = 0;
		final int METHOD_NO = 1;
		final int SUMMARY_NO = 2;
		final int DESCRIPTION_NO = 3;
		final int TAGS_NO = 4;
		final int DEPRECATED_NO = 5;

		int row;
		int row_sumaary_se;
		int row_operationid_se;
		int row_tags_s;
		int row_tags_e;
		int row_description_s;
		int row_description_e;
		boolean description_flg;
		boolean tags_flg;

		String tmp_txt;

		String[][] arrayData = new String[files.size()][6];
		// fileOpen
		try {
			for (int i = 0; i < files.size(); i++) {

				row = 0;
				row_sumaary_se = 0;
				row_operationid_se = 0;
				row_tags_s = 0;
				row_tags_e = 0;
				row_description_s = 0;
				row_description_e = 0;
				description_flg = false;
				tags_flg = false;

				// System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
				// System.out.println(files.get(i));
				List<String> lines = Files.readAllLines((files.get(i)), StandardCharsets.UTF_8);
				for (String s : lines) {

					if (s.indexOf(SUMMARY) != -1) {
						if (s.indexOf(ESCAPE) != -1) {
							row_sumaary_se = row + 1;
						} else {
							row_sumaary_se = row;
						}
					} else if (s.indexOf(TAGS) != -1) {
						row_tags_s = row;
						tags_flg = true;
					} else if (tags_flg && s.indexOf(":") != -1) {
						row_tags_e = row - 1;
						tags_flg = false;
					}

					if (s.indexOf(OPERATIONID) != -1) {
						row_operationid_se = row;
					}

					if (s.indexOf(DESCRIPTION) != -1) {

						if (s.indexOf(ESCAPE) != -1) {
							row_description_s = row + 1;
						} else {
							row_description_s = row;
						}
						description_flg = true;
					} else if (description_flg && s.indexOf(":") != -1) {
						row_description_e = row - 1;
						description_flg = false;
					}

					if (row_sumaary_se != 0 && row_tags_s != 0 && row_operationid_se != 0 && row_tags_e != 0
							&& row_description_s != 0 && row_description_e != 0) {
						// 一度
						break;
					}
					row++;
				}

				// 初期化
				System.out.println(lines.get(0).replace(":", "").trim().toUpperCase());

				// System.out.println(arrayData[i][METHOD_NO]);
				arrayData[i][METHOD_NO] = lines.get(0).replace(":", "").trim().toUpperCase();
				// System.out.println(arrayData[i][METHOD_NO]);

				arrayData[i][METHOD_NO] = lines.get(0).replace(":", "").trim().toUpperCase();
				arrayData[i][SUMMARY_NO] = lines.get(row_sumaary_se).replace(SUMMARY, "").trim();
				arrayData[i][OPERATIONID_NO] = lines.get(row_operationid_se).replace(OPERATIONID, "").trim();

				// System.out.println(lines.get(0).replace(":", "").trim().toUpperCase());
				// System.out.println(lines.get(row_sumaary_se).replace(SUMMARY, "").trim());
				// System.out.println(lines.get(row_operationid_se).replace(OPERATIONID,
				// "").trim());

				tmp_txt = "";
				for (int t = row_tags_s + 1; t <= row_tags_e; t++) {
					// System.out.println(lines.get(t).replace(TAGS, "").trim());
					if (tmp_txt.isEmpty()) {
						tmp_txt = lines.get(t).replace(TAGS, "").trim();
					} else {
						tmp_txt = tmp_txt + "\n" + lines.get(t).replace(TAGS, "").trim();
					}
					arrayData[i][TAGS_NO] = tmp_txt;
				}

				if (row_description_s != 0) {
					tmp_txt = "";
					for (int t = row_description_s; t <= row_description_e; t++) {
						if (tmp_txt.isEmpty()) {
							tmp_txt = lines.get(t).replace(DESCRIPTION, "").trim();
						} else {

							tmp_txt = tmp_txt + "\n" + lines.get(t).replace(DESCRIPTION, "").trim();
						}
						// System.out.println(lines.get(t).replace(DESCRIPTION, "").trim());
					}

					arrayData[i][DESCRIPTION_NO] = tmp_txt;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayData;
	}

	public static String[][] setarray(ArrayList<Path> files) {

		final String SUMMARY = "summary:";
		final String TAGS = "tags:";
		final String DESCRIPTION = "description:";
		final String OPERATIONID = "operationId:";
		final String ESCAPE = "|-";

		final int OPERATIONID_NO = 0;
		final int METHOD_NO = 1;
		final int SUMMARY_NO = 2;
		final int DESCRIPTION_NO = 3;
		final int TAGS_NO = 4;
		final int DEPRECATED_NO = 5;

		int row;
		int row_sumaary_se;
		int row_operationid_se;
		int row_tags_s;
		int row_tags_e;
		int row_description_s;
		int row_description_e;
		boolean description_flg;
		boolean tags_flg;

		String tmp_txt;

		// 配列に突っ込んでarraylistに突っ込んで、最後に配列に入れる
		System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
		String[] Data = new String[6];
		Data[0] = "1ddd";
		Data[1] = "2ddd";
		Data[2] = "3ddd";
		Data[3] = "4ddd";
		Data[4] = "5ddd";
		Data[5] = "6ddd";
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(Data);
		System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
		System.out.println("配列 = " + Arrays.asList(list));

		String[][] arrayData = new String[files.size()][6];
		// arrayData[0] = list.toArray();
		arrayData[0] = list.get(0);
		return arrayData;
	}

	@SuppressWarnings("null")
	public static String[][] setarrayS(Path files) {

		final String PATHS = "/";
		final String SUMMARY = "summary:";
		final String TAGS = "tags:";
		final String DESCRIPTION = "description:";
		final String DEPRECATED = "deprecated:";
		final String OPERATIONID = "operationId:";
		final String ESCAPE = ":";
		final String CATEGORY_PO = "post:";
		final String CATEGORY_G = "get:";
		final String CATEGORY_PU = "put:";
		final String CATEGORY_D = "delete:";

		final int OPERATIONID_NO = 0;
		final int METHOD_NO = 1;
		final int SUMMARY_NO = 2;
		final int DESCRIPTION_NO = 3;
		final int TAGS_NO = 4;
		final int DEPRECATED_NO = 5;

		int row;
		int line;
		int row_sumaary_se;
		int row_operationid_se;
		int row_path_se = 0;
		int row_tags_s = 0;
		int row_tags_e = 0;
		int row_description_s;
		int row_description_e;
		boolean description_flg;
		boolean tags_flg;
		boolean deprecated_flag;

		String tmp_txt;
		String[][] arrayData = null;
		deprecated_flag = false;
		tags_flg = false;

		// fileOpen
		try {
			row = -1;
			line = 0;

			List<String> lines = Files.readAllLines((files), StandardCharsets.UTF_8);

			for (String s : lines) {
				switch (s.trim()) {
				case CATEGORY_G:
				case CATEGORY_PU:
				case CATEGORY_PO:
				case CATEGORY_D:
					line++;
					break;
				default:
				}
			}
			arrayData = new String[line][6];
			line = -1;

			for (String s : lines) {
				line++;

				if (s.trim().startsWith(PATHS)) {
					row_path_se = line;
					// System.out.println(line+"行目："+s.trim());
				} else if (s.indexOf(TAGS) != -1) {
					row_tags_s = line;
					tags_flg = true;
				} else if (tags_flg && s.indexOf(":") != -1) {
					row_tags_e = line - 1;
					tags_flg = false;
					tmp_txt = "";
					System.out.println(line + "行：" + s);
					for (int t = row_tags_s + 1; t <= row_tags_e; t++) {
						// System.out.println(lines.get(t).replace(TAGS, "").trim());
						if (tmp_txt.isEmpty()) {
							tmp_txt = lines.get(t).replace(TAGS, "").trim();
						} else {
							tmp_txt = tmp_txt + "\n" + lines.get(t).replace(TAGS, "").trim();
						}
						arrayData[row][TAGS_NO] = tmp_txt;
					}

				}

				if (s.trim().startsWith(SUMMARY)) {
					arrayData[row][SUMMARY_NO] = s.replace(SUMMARY, "").trim();
					arrayData[row][DESCRIPTION_NO] = s.replace(SUMMARY, "").trim();
					deprecated_flag = false;
				}
				if (s.trim().startsWith(DEPRECATED)) {
					deprecated_flag = true;
					arrayData[row][DEPRECATED_NO] = s.replace(DEPRECATED, "").trim().toString();
				}
				// DEPRECATED_NO

				switch (s.trim()) {
				case CATEGORY_G:
				case CATEGORY_PU:
				case CATEGORY_PO:
				case CATEGORY_D:
					row++;
					arrayData[row][OPERATIONID_NO] = lines.get(row_path_se).replace(ESCAPE, "").trim();
					arrayData[row][METHOD_NO] = s.replace(":", "").trim().toUpperCase();

					System.out.println(row_path_se);
					System.out.println(row + arrayData[row][DESCRIPTION_NO] + "：" + arrayData[row][METHOD_NO]);

					break;
				default:
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayData;
	}
}
