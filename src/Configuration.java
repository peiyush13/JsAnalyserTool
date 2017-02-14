import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by piyush on 1/27/2017.
 */
public class Configuration  {

    private static String path = "C:\\Program Files\\nodejs\\node_modules\\eslint\\conf\\eslint.json";

    public static void configure_rule(String[] rules,int option) throws IOException {


        int value_option;
        String option_string = null;
        Scanner sc = new Scanner(System.in);

        System.out.println("selected rule"+ rules[option]);
        System.out.println("present mode of the rule : "+ Test.Mapper.get(rules[option]));
        System.out.println("select mode of the rule :");
        System.out.println("1. Off  2. Error  3. Warn");
        value_option=sc.nextInt();

        switch (value_option)
        {
            case 1: option_string="Off";
                break;
            case 2: option_string="Error";
                break;
            case 3: option_string="Warn";
                break;
        }

        Test.Mapper.put(rules[option],option_string);
        JSONObject json_file =Test.read_json();
        JSONObject old_rules= (JSONObject) json_file.get("rules");

        old_rules.put(rules[option],option_string);
        json_file.put("rules",old_rules);

        FileWriter fw= new FileWriter(Test.current_path+"\\.eslintrc.json");
        fw.write(json_file.toString(4));
        fw.flush();
        JSONObject jsonfile= new JSONObject();

        for (Map.Entry<String, Object> entry : Test.Mapper.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonfile.put(key,value);

        }
        json_file.put("rules",jsonfile);
        FileWriter fw1= new FileWriter(Test.current_path+"\\rules.json");
        fw1.write(json_file.toString(4));
        fw1.flush();

    }

}
