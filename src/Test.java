import groovy.json.JsonException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Test {

    public static Map<String, Object> Mapper;
    static Path currentRelativePath = Paths.get("");
    public static String current_path = currentRelativePath.toAbsolutePath().toString();


    public  static void main(String args[]) throws IOException {
        int i=4;
        int option = 0;
        Scanner sc = new Scanner(System.in);
        initialization();
        do {
            int rule_option,choice;

            JSONObject arr = null;
            arr = new JSONObject(readFile(current_path + "\\rules.json"));


            JSONObject obj = (JSONObject) arr.get("rules");
            Mapper = jsonToMap(obj);

            Set<String> strings = Mapper.keySet();
            String[] rules = strings.toArray(new String[strings.size()]);


            System.out.println("1. testing js file");
            System.out.println("2. configuring rules");
            System.out.println("Enter your choice");
            choice=sc.nextInt();
            switch (choice)
            {
                case 1 : cmd_execution();
                         testing();
                    break;

                case 2: Map<Integer, Integer> m = display(rules);
                        rule_option = sc.nextInt();
                        rule_option = m.get(rule_option);
                        Configuration.configure_rule(rules, rule_option);
                    break;
                default :
                    System.out.println("wrong choice");
                    break;
            }


            System.out.println("Do you want to continue : ");
            System.out.println("1: Yes  2. No");
            option=sc.nextInt();

        }while(option!=2);
    }

    public static JSONObject read_json() throws IOException {

        JSONObject arr;
         arr = new JSONObject(readFile(current_path+"\\.eslintrc.json"));
         return arr;
    }

    public static String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);

            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){reader.close();}
        }
        return content;
    }


    public static Map<Integer,Integer> display(String[] rules){

        Map<Integer,Integer> map2 = new HashMap<Integer, Integer>();
        int count=1,i;
        System.out.println("Default Rules : (shown as Error)" );
        for(i=0;i<rules.length;i++)
            if((Mapper.get(rules[i])).equals("error")) {
                map2.put(count,i);
                System.out.println(count + "." + rules[i]);
                count++;
            }
//        System.out.println("Warning Rules : (shown as Warning)" );
//        count=1;
//        for(i=0;i<rules.length;i++)
//            if((Mapper.get(rules[i])).equals("warn")) {
//                System.out.println(count + "." + rules[i]);
//                count++;
//            }
//        System.out.println("Diasabled Rules : (turned off at the moment)" );
//        count=1;
//        for(i=0;i<rules.length;i++)
//            if((Mapper.get(rules[i])).equals("off")) {
//                System.out.println(count + "." + rules[i]);
//                count++;
//            }
        return map2;
    }

    public static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json!=null) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JsonException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keySet().iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static void cmd_execution(){
        try {
            String path = current_path+"\\test_js";
            String command = "cmd /c DIR "+path+"\\**";
            Process child = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(child.getInputStream()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line+"\n");
            }
            System.out.println(out.toString());   //Prints the string content read from input stream
            reader.close();

        } catch (IOException e) {
        }


    }

    public static void init_rules() throws IOException {
        JSONObject object=new JSONObject("{\n" +
                "    \"env\": {\n" +
                "        \"browser\": true\n" +
                "    },\n" +
                "    \"extends\": \"eslint:recommended\",\n" +
                "    \"rules\": {\n" +
                "    }\n" +
                "}");
        System.out.println();
        FileWriter fw= new FileWriter(current_path+"\\.eslintrc.json");
        fw.write(object.toString(4));
        fw.flush();
    }

    public static void initialization() throws IOException{
        JSONObject arr;
        arr = new JSONObject(readFile(current_path+"\\original rules files\\rules.json"));
        init_rules();
        JSONObject json_file= new JSONObject();
        json_file.put("rules",arr.get("rules"));
        FileWriter fw= new FileWriter(current_path+"\\rules.json");
        fw.write(json_file.toString(4));
        fw.flush();
        System.out.println("JS analyser is successfully configured :)");
    }

    public static void testing() {
        String option;
        Scanner sc= new Scanner(System.in);
        option=sc.nextLine();

        try {
            String path = current_path+"\\test_js";
            String command = "cmd /c eslint "+path+"\\"+option;
            Process child = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(child.getInputStream()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line+"\n");
            }
            System.out.println(out.toString());   //Prints the string content read from input stream
            reader.close();

        } catch (IOException e) {
        }
    }
}

