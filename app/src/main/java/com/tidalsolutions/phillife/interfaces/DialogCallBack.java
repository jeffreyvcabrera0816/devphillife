package com.tidalsolutions.phillife.interfaces;

import java.io.Serializable;

/**
 * Created by SantusIgnis on 21/07/2016.
 */
public interface DialogCallBack extends Serializable {

    void dialogfunction(int position, int position2, Object data1, Object data2);

}
