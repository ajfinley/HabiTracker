package com.jadeinc.habitracker;

import java.util.List;

/**
 * Created by evan on 4/20/17.
 */
public interface DBListener {

    void onSuccess(List<User> users);

}
