package kr.study.capston2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JI on 2018-10-09.
 */
////////////////LoginActivity 와 서버 연동하는 엑티비티
public class LoginRequest extends StringRequest {

    final static private String URL = "http://wltjrals45.cafe24.com/Login.php";
    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPassword",userPassword);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
