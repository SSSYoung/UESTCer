package test.example.com.uestcer.utils;

import test.example.com.uestcer.view.BaseFragment;
import test.example.com.uestcer.view.ContactFragment;
import test.example.com.uestcer.view.ConversationFragment;
import test.example.com.uestcer.view.PlugInFragment;

/**
 * Created by DK on 2017/5/5.
 */

public class FragmentFactory {
    private static ConversationFragment conversationFragment=null;
    private static ContactFragment contactFragment=null;
    private static PlugInFragment plugInFragment=null;

    public static BaseFragment getFragment(int position){
        BaseFragment baseFragment=null;
        switch (position){
            case 0:
                if (conversationFragment==null){
                    //没有就new一个
                    conversationFragment=new ConversationFragment();
                }
                baseFragment=conversationFragment;
                break;
            case 1:
                if (contactFragment==null){
                    contactFragment=new ContactFragment();
                }
                baseFragment=contactFragment;
                break;
            case 2:
                if (plugInFragment==null){
                    plugInFragment=new PlugInFragment();
                }
                baseFragment=plugInFragment;
                break;
        }
        return baseFragment;
    }
}
