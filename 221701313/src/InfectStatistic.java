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
    private static String date_;
    //日志路径
    private static String log_;
    //output文件路径
    private static String out_;
    //需输出的类型
    private static String[] type_;
    //需输出的省份
    private static List<String> province_;
    //输出所有type的信息
    private static boolean AllType;
    //输出所有province的信息
    private static boolean AllProvince;
    //参数province_的映射关系
    private HashMap<String,Province> map;
    //参数type_的映射关系
    @SuppressWarnings("serial")
	public static final Map<String, String> typeMap = new HashMap<String, String>() {{
        put("ip", "感染患者");
        put("sp", "疑似患者");
        put("cure", "治愈");
        put("dead", "死亡");
    }};
    //省份名称
    private static final List<String> provinceList = Arrays.asList("全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西",
            "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏",
            "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江");
    
    //构造函数
    public InfectStatistic(){
        type_ = new String[10];
        AllType = true;
        AllProvince = true;
        province_ = new ArrayList<>();
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
						date_ = new String(args_[i]);
	                    break;
    				case "-log":
    					log_ = args_[i];
    					File file = new File(log_);
    			        if (!file.exists()) {
    			        	System.out.print("-log错误!--");
    			        	return false;
    			        }
                        break;
    				case "-out":
    					out_ = args_[i];
                        break;
    				case "-type":
    					AllType = true;
    					for(int j = 0;!args_[i].startsWith("-");j++,i++) {
    						if(!typeMap.containsKey(args_[i])) {
    							System.out.print("-type错误!--");
    							return false;
    						}
    						else
    							type_[j] = typeMap.get(args_[i]);
    					}
    					i--;
                        break;
    				case "-province":
    					AllProvince = false;
    					for(int j = 0; i < args_.length && !args_[i].startsWith("-");j++,i++) {
    						if(provinceList.contains(args_[i])) {
    							province_.add(args_[i]);
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
    public boolean Read_Deal_File() throws IOException {  
    	boolean b = false;
        String filename;
        String[] filedate;
        File file = new File(log_);
        File[] tempList = file.listFiles();
        for(int i=0;i < tempList.length;i++) {
	        filename = new String(tempList[i].getName());
	        filedate = filename.split("\\.");
	        if(date_.equals(filedate[0])) {
	        	BufferedReader br = null;               
                String dates = null;
                br = new BufferedReader(new InputStreamReader(new FileInputStream(tempList[i].toString()), "UTF-8"));  
                while ((dates = br.readLine()) != null){
                	Deal_Dates_Line(dates);
                }
                b = true;
                br.close();
                break;
	        }
        }
        map.put(provinceList.get(0), new Province(provinceList.get(0)));
        for (int i = 0; i < provinceList.size(); i ++ )
        {
            if (map.get(provinceList.get(i)) != null)
            	map.get(provinceList.get(0)).Statistics(map.get(provinceList.get(i)));
        }
        return b;
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
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_), "UTF-8"));
        //无-province参数，默认输出所有省情况
        if (AllProvince)
        {
            for (int i = 0;i < provinceList.size();i++)
                if (map.get(provinceList.get(i)) != null)
                    map.get(provinceList.get(i)).output(AllType,type_ ,bw);
        }
        else
        {
            for (int i = 0; i < provinceList.size(); i ++ )
            { 
                if (province_.contains(provinceList.get(i)))
                    map.get(provinceList.get(i)).output(AllType,type_ ,bw);
            }
        }
        bw.write("// 该文档并非真实数据，仅供测试使用");
        bw.close();
    }
    
        
    public static void main(String[] args) throws IOException {
    	//String[] aa= {"list", "-date","2020-01-22","-log" ,"C:\\Users\\东伯\\Desktop\\123","-out","D:\\ouput.txt","-type","ip","cure","-province","全国","福建","湖北"};
    	InfectStatistic infectStatistic = new InfectStatistic();
    	if (!Verify_Init_args(args)) {
    		System.out.println("参数错误 ");
    		return ;
    	}
    	if(!infectStatistic.Read_Deal_File()) {
    		System.out.println("路径: "+log_+"下不存在" +date_+ ".log.txt!");
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
	public void output(boolean alltype, String[] type, BufferedWriter bw) throws IOException
	{
	    //无-type参数，默认输出所有type类型
	    if (alltype)
	    {
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
