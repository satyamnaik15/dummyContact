package dummy.demo.test.dummy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click=(Button) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(getAssets().open("names.txt"), "UTF-8"));
                    // do reading, usually loop until end of file reading
                    StringBuilder sb = new StringBuilder();
                    String mLine = reader.readLine();
                    while (mLine != null) {
                        try {
                            sb.append(mLine); // process line
                            mLine = reader.readLine();
                            if(mLine!=null)
    //                        Log.e("----- ","========= "+(mLine.replace(","," ")).replace("\"",""));
                            mLine=(mLine.replace(","," ")).replace("\"","");
                            ArrayList<ContentProviderOperation>   aOps = new ArrayList<ContentProviderOperation>();
                            int aRawContactID = aOps.size();
                            aOps.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)

                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null)
                                    .withValue(ContactsContract.RawContacts.AGGREGATION_MODE, ContactsContract.RawContacts.AGGREGATION_MODE_DEFAULT)
                                    .build());

                            // Adding insert operation to operations list
                            // to insert display name in the table ContactsContract.Data
                            aOps.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, aRawContactID)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, mLine)
                                    .build());


                            aOps.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, aRawContactID)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "112121")
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME).build());


                            MainActivity.this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, aOps);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Done !!!!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
