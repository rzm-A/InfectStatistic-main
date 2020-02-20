import java.io.*;
import java.util.*;

/**
 * InfectStatistic
 * TODO
 *
 * @author rzm
 * @version  
 * @since  
 */
class InfectStatistic {
    //接收命令行参数
    private String[] args;
    //日志日期
    private static String date_p;
    //日志路径
    private static String log_p;
    //output文件路径
    private static String out_p;
    //需输出的类型
    private static String[] type_p;
    //需输出的省份
    private static List<String> province_p;
    //输出所有type的信息
    private static boolean AllType;
    //输出所有province的信息
    private static boolean AllProvince;
    //参数province_p的映射关系
    private HashMap<String,Province> map;
    //参数type_p的映射关系
    @SuppressWarnings("serial")
	public static final Map<String, String> typeMap = new HashMap<String, String>() {{
        put("ip", "感染患者");
        put("sp", "疑似患者");
        put("cure", "治愈");
        put("dead", "死亡");
    }};
    //省份名称
    private static final List<String> provinceList = Arrays.asList("全国", "安徽", "北京", "重庆", "福建", "甘肃",
    		"广东","广西","贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", 
    		"内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江");
    
    //构造函数
    public InfectStatistic(){
    	type_p = new String[10];
        AllType = true;
        AllProvince = true;
        province_p = new ArrayList<>();
        map = new HashMap<String,Province>();
        for (int i = 0; i < provinceList.size(); i ++ )
        	map.put(provinceList.get(i), null);
    }
    
	//验证命令行传入参数的正误,初始化文件路径等
    public static boolean Verify_Init_args(String[] args_) throws IOException {
    	if(args_[0].equals("list")){
    		for (int i = 1; i < args_.length; i++) {
    			i++;
    			switch (args_[i-1]) {
					case "-date":
						date_p = new String(args_[i]);
	                    break;
    				case "-log":
    					log_p = args_[i];
    					File file = new File(log_p);
    			        if (!file.exists()) {
    			        	System.out.print("-log错误!--");
    			        	return false;
    			        }
                        break;
    				case "-out":
    					out_p = args_[i];
                        break;
    				case "-type":
    					AllType = true;
    					for(int j = 0;!args_[i].startsWith("-");j++,i++) {
    						if(!typeMap.containsKey(args_[i])) {
    							System.out.print("-type错误!--");
    							return false;
    						}
    						else
    							type_p[j] = typeMap.get(args_[i]);
    					}
    					i--;
                        break;
    				case "-province":
    					AllProvince = false;
    					for(int j = 0; i < args_.length && !args_[i].startsWith("-");j++,i++) {
    						if(provinceList.contains(args_[i])) {
    							province_p.add(args_[i]);
    						}
    						else
    							System.out.print("-province错误!--");
    					}
    					i--;
                        break;
    				default:
                            return false;
                }
            }
    		return true;
        }
    	return false;
    }
    
    //验证文件是否存在,不存在返回false，存在则读取处理文件数据返回true
    @SuppressWarnings("null")
	public boolean Read_Deal_File() throws IOException {  
    	boolean b = false;
        String filename;
        String[] filedate;
        String[] name = new String[100];
        int[] num = new int[100];
        int i,j;
        File file = new File(log_p);
        File[] tempList = file.listFiles();
        //选取符合命名的文件
        for(i=0,j = 0;i < tempList.length;i++) {
        	filename = new String(tempList[i].getName());
	        filedate = filename.split("\\.");
	        boolean b1,b2,b3,b4;
	        if(filedate[0].length() != 10)
	        	continue;
	        if(filedate[0].charAt(4) != '-' || filedate[0].charAt(7) != '-')
	        	continue;
	        b1 = Date_Isdit(filedate[0],0,1);
	        b2 = Date_Isdit(filedate[0],2,3);
	        b3 = Date_Isdit(filedate[0],5,6);
	        b4 = Date_Isdit(filedate[0],8,9);
	        if(b1 && b2 && b3 && b4 && filedate[0] !=null) {
	        	name[j] = filedate[0];
	        	num[j++] = i;
	        }
        }
        for(i = 0;i < j;i++) {
        	if(date_p.compareTo(name[i]) >= 0) {
        		BufferedReader br = null;               
                String dates = null;
                br = new BufferedReader(new InputStreamReader(new FileInputStream(tempList[num[i]].toString()), "UTF-8"));  
                while ((dates = br.readLine()) != null){
                	Deal_Dates_Line(dates); 
                }
                b = true;
                br.close();
        	}
        }
        map.put(provinceList.get(0), new Province(provinceList.get(0)));
        for (i = 0; i < provinceList.size(); i ++ ){
            if (map.get(provinceList.get(i)) != null)
            	map.get(provinceList.get(0)).Statistics(map.get(provinceList.get(i)));
        }
        return b;
    }

    //检验是否是数字
    private boolean Date_Isdit(String str,int num1,int num2) {
    	if(!Character.isDigit(str.charAt(num1)) || !Character.isDigit(str.charAt(num2))) {
        	return false;
        }
    	return true;
    }
    
    //处理文件数据（单行）
    private void Deal_Dates_Line(String dates) throws IOException {
    	String[] date = dates.split(" ");
        if (map.get(date[0]) == null)
            map.put(date[0], new Province(date[0]));
        else if (date[0].equals("//"))
	        return;
        
        switch(date[1])
        {
            case "新增":
                if (date[2].equals("感染患者"))
                    map.get(date[0]).add_Ip(date[3]);
                else
                    map.get(date[0]).add_Sp(date[3]);
                break;
            case "感染患者":
                if (map.get(date[3]) == null)
                    map.put(date[3], new Province(date[3]));
                map.get(date[3]).add_Ip(date[4]);
                map.get(date[0]).reduce_Ip(date[4]);
                break;
            case "疑似患者":
                if (date[2].equals("流入")){
                    if (map.get(date[3]) == null)
                        map.put(date[3], new Province(date[3]));
                    map.get(date[3]).add_Sp(date[4]);
                    map.get(date[0]).reduce_Sp(date[4]);
                }
                else{
                    map.get(date[0]).add_Ip(date[3]);
                    map.get(date[0]).reduce_Sp(date[3]);
                }
                break;
	        case "死亡":
	        	map.get(date[0]).reduce_Ip(date[2]);
	            map.get(date[0]).dead(date[2]);
	            break;
	        case "治愈":
	        	map.get(date[0]).reduce_Ip(date[2]);
	            map.get(date[0]).cure(date[2]);
	            break;
	        case "排除":
	            map.get(date[0]).reduce_Sp(date[3]);
	            break;
            default:
                break;
        }
    }
    
    //根据要求生成输出文件
    public void Output_File() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_p), "UTF-8"));
        //无-province参数，默认输出所有省情况
        if (AllProvince)
        {
            for (int i = 0;i < provinceList.size();i++)
                if (map.get(provinceList.get(i)) != null)
                    map.get(provinceList.get(i)).output(AllType,type_p ,bw);
        }
        else
        {
            for (int i = 0; i < provinceList.size(); i ++ )
            { 
                if (province_p.contains(provinceList.get(i)))
                    map.get(provinceList.get(i)).output(AllType,type_p ,bw);
            }
        }
        bw.write("// 该文档并非真实数据，仅供测试使用");
        bw.close();
    }
    
        
    public static void main(String[] args) throws IOException {
    	//String[] aa= {"list", "-date","2020-01-27","-log" ,"C:\\Users\\东伯\\Desktop\\123",
    	//		"-out","D:\\ouput.txt","-type","ip","cure","-province","全国","福建","湖北"};
    	InfectStatistic infectStatistic = new InfectStatistic();
    	if (!Verify_Init_args(args)) {
    		System.out.println("参数错误 ");
    		return ;
    	}
    	if(!infectStatistic.Read_Deal_File()) {
    		System.out.println("路径: "+log_p+"下不存在" +date_p+ ".log.txt!");
    		return ;
    	}
    	infectStatistic.Output_File();
    }
    
}

//某个省份的具体数据
class Province {
	//省份名称
	private String provincename;
	//疑似患者人数
	private int sp;
	//感染患者人数
	private int ip;
	//治愈人数
	private int cure;
	//死亡人数
	private int dead;
	
	//构造函数
	public Province(String provincename){
		this.provincename = provincename;
		sp = 0;
		ip = 0;
		cure = 0;
		dead = 0;
	}
	
	//感染患者增加
	public void add_Ip(String str){
		ip += Integer.parseInt(str.substring(0, str.length()-1));
	}
		
	//疑似患者增加
	public void add_Sp(String str){
	    sp += Integer.parseInt(str.substring(0, str.length()-1));
	}

	//感染患者减少
	public void reduce_Ip(String str){
	    ip -= Integer.parseInt(str.substring(0, str.length()-1));
	}
	
	//疑似患者减少
	public void reduce_Sp(String str){
	    sp -= Integer.parseInt(str.substring(0, str.length()-1));
	}
	
	//死亡
	public void dead(String str){
		dead += Integer.parseInt(str.substring(0, str.length()-1));
	}  
	
	//治愈
	public void cure(String str){
		cure += Integer.parseInt(str.substring(0, str.length()-1));
	}
	
	//统计全国数据
	public void Statistics(Province p){
	    this.ip += p.ip;
	    this.sp += p.sp;
	    this.cure += p.cure;
	    this.dead += p.dead;
	}
	
	//输出指定省的信息
	public void output(boolean alltype, String[] type, BufferedWriter bw) throws IOException{
	    //无-type参数，默认输出所有type类型
	    if (alltype){
	        bw.write(provincename+ " 感染患者 " +ip+ "人 疑似患者 " +sp+ "人  治愈 " 
	        		+cure+ "人 死亡 " +dead+ "人");
	        bw.newLine();
	    }
	    else
	      {
	          bw.write(provincename);
	          for (int i = 0; i < type.length; i ++ ){
	        	  bw.write(" " +type[i]);
	        	  switch (type[i])
	              {
	        	  	case "感染患者":                  
	                    bw.write(ip+ "人");           
	                    break;
	                case "疑似患者":                  
	                    bw.write(sp+ "人");               
	                    break;
	                case "治愈":                    
	                    bw.write(cure+ "人");                  
                        break;
                    case "死亡":                    
                        bw.write(dead+ "人");          
                        break;
                    default:                
                        break;
	                }
	          }
	          bw.newLine();
	      }       
	  }
}
