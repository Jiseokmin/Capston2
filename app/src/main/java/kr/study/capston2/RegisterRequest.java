package kr.study.capston2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JI on 2018-10-09.
 */

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://wltjrals45.cafe24.com/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userPassword2, String userName ,int userAge, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("userPassword",userPassword);
        parameters.put("userPassword2",userPassword2);
        parameters.put("userName",userName);
      //  parameters.put("userMail",userMail);
        parameters.put("userAge",userAge + "");
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
