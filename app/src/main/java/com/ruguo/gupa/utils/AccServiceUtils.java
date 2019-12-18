package com.ruguo.gupa.utils;

import android.view.accessibility.AccessibilityNodeInfo;

public class AccServiceUtils {

    public static AccessibilityNodeInfo findInfoById(AccessibilityNodeInfo accessibilityNodeInfo ,String id){

        if(accessibilityNodeInfo != null){
            if(accessibilityNodeInfo.getChildCount() == 0){
                 if(!StringUtils.isEmpty(accessibilityNodeInfo.getViewIdResourceName()) &&  accessibilityNodeInfo.getViewIdResourceName().equals(id)){
                     return  accessibilityNodeInfo;
                 }
            }else{
                for(int i = 0 ; i < accessibilityNodeInfo.getChildCount(); i ++){
                    if(!StringUtils.isEmpty(accessibilityNodeInfo.getViewIdResourceName()) &&  accessibilityNodeInfo.getViewIdResourceName().equals(id)){
                        return  accessibilityNodeInfo;
                    }
                    findInfoById(accessibilityNodeInfo.getChild(i),id);
                }
            }
        }
        return null;
    }

}
