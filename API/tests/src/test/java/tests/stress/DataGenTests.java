package tests.stress;

import com.ffan.qa.utils.FileUtil;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.Map;

public class DataGenTests extends TestBase {
    @Test(dataProvider = "data")
    public void genQfLoginModelTests(Map<String, Object> data) throws Exception {
        qfLoginForDP(data);
        String qfFileName = "qfUsers.txt";
        FileUtil.createFile(qfFileName);
        String content = String.format(
                "%s,%s,%s,%s,%s,%s",
                data.get("plazaId").toString(),
                data.get("plazaName").toString(),
                qfLoginModel.getTenantId(),
                qfLoginModel.getToken(),
                qfLoginModel.getUserId(),
                data.get("user").toString());
        FileUtil.writeFileContent(qfFileName, content);
    }
}
