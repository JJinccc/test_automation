package tests.members;

import com.ffan.qa.common.Logger;
import com.ffan.qa.settings.TestConfig;
import com.ffan.qa.utils.ExcelUtil;
import com.ffan.qa.utils.ObjectUtil;
import org.testng.annotations.DataProvider;
import tests.TestBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BMemberTestBase extends TestBase {
    @DataProvider(name = "memberData")
    public Object[][] getMemberData(Method method) {
        Object[][] data = super.getData(method);
        List<Object> arr = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            Map<String, Object> d = (HashMap<String, Object>)data[i][0];
            if (Boolean.parseBoolean(d.get("memberSupport").toString())) {
                arr.add(data[i][0]);
            }
        }

        Object[][] retData = new Object[arr.size()][1];
        for (int i = 0; i < arr.size(); i++) {
            retData[i][0] = arr.get(i);
        }

        return retData;
    }
}
