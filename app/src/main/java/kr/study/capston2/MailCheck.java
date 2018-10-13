package kr.study.capston2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JI on 2018-10-14.
 */
////////////////메일 중복 체크 하는 서버 연동
public class MailCheck extends StringRequest {

    final static private String URL = "http://wltjrals45.cafe24.com/MailCheck.php";
    private Map<String, String> parameters;

    public MailCheck(String userMail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userMail",userMail);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
