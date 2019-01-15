package BottomNavigation;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ok.pinjamlaptop.R;

import LoginRegister.DatabaseHelper;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    ImageView ivBackgroundImage, ivDropDown;
    ImageButton ibPP;
    String whoCalledActivityResult;
    public static int RESULT_LOAD_IMAGE = 123;
    public final static int PHOTO_PERMISSION = 100;
//    String emailLogin;
    TextView tvEmail, t_error_message;
    EditText eOldPassword, eNewPassword;
    AlertDialog dialog;
    ProgressBar progress;
    DatabaseHelper databaseHelper;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivBackgroundImage = (ImageView)view.findViewById(R.id.header_cover_image);
        ivDropDown = (ImageView)view.findViewById(R.id.drop_down_option_menu);
        ibPP = (ImageButton)view.findViewById(R.id.user_profile_photo);
        tvEmail = (TextView)view.findViewById(R.id.user_profile_short_bio);
        databaseHelper = new DatabaseHelper(getActivity());
        ivBackgroundImage.setOnClickListener(this);
        ivDropDown.setOnClickListener(this);
        ibPP.setOnClickListener(this);

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tool_bar_fragment);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        getActivity().setTitle(null);
        setHasOptionsMenu(true);

        GlobalVariable globalVariable = ((GlobalVariable)getActivity().getApplicationContext());
        tvEmail.setText(globalVariable.getcEmail());
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu.findItem(R.id.item_search_data) != null) {
            menu.findItem(R.id.item_search_data).setVisible(false);
        }

        super.onPrepareOptionsMenu(menu);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_profile_photo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                whoCalledActivityResult = "ibPP";
                break;
            case R.id.header_cover_image:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                whoCalledActivityResult = "ivBackgroundImage";
                break;
            case R.id.drop_down_option_menu:
                PopupMenu popup = new PopupMenu(getActivity(), ivDropDown);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.iChangePass:
                                showChgPassDialog();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                if(!(getActivity().isFinishing()))
                {
                    popup.show();
                }
//                popup.show();
                break;
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PHOTO_PERMISSION);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (whoCalledActivityResult.equals("ibPP")) {
                ibPP.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } else if (whoCalledActivityResult.equals("ivBackgroundImage")) {
                ivBackgroundImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }

    private void showChgPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        eOldPassword = (EditText)view.findViewById(R.id.etOldPassword);
        eNewPassword = (EditText)view.findViewById(R.id.etNewPassword);
        t_error_message = (TextView)view.findViewById(R.id.tv_error_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);

        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = eOldPassword.getText().toString();
                String new_password = eNewPassword.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty()) {
                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(old_password, new_password);
                } else {
                    t_error_message.setVisibility(View.VISIBLE);
                    t_error_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changePasswordProcess(String old_password, String new_password) {
        String email = tvEmail.getText().toString();
        String oldpass = databaseHelper.getPass(email);
        if (oldpass.equals(old_password)) {
            if (!oldpass.equals(new_password)) {
                databaseHelper.changePass(email, new_password);
                eOldPassword.setText("");
                eNewPassword.setText("");
                Toast.makeText(getActivity(), "Password Changed!", Toast.LENGTH_LONG).show();
            } else {
                t_error_message.setVisibility(View.VISIBLE);
                t_error_message.setText("Old Password and New Password must not be the same");
            }
        } else {
            t_error_message.setVisibility(View.VISIBLE);
            t_error_message.setText("Invalid Old Password");
        }

        progress.setVisibility(View.GONE);
    }

}
