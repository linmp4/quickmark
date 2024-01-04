package cwp.moneycharge.model;

import java.util.LinkedList; 
import java.util.List; 
import android.app.Activity; 
import android.app.AlertDialog; 
import android.app.Application; 
import android.content.DialogInterface; 
import android.content.Intent; 

public class ActivityManager {
	private List<Activity> mList = new LinkedList<Activity>(); 
    private static ActivityManager instance; 
	 
	public ActivityManager() {
		// TODO Auto-generated constructor stub
	}

 
    public synchronized static ActivityManager getInstance() { 
        if (null == instance) { 
            instance = new ActivityManager(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    }  
}