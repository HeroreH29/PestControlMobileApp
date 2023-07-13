package com.example.edpestcontrolservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConsultationForm extends AppCompatActivity {

    private Spinner spinner;
    private EditText et1, et2, et3, et4;
    private RadioGroup rd1, rd2, rd3, rd4;
    private RadioButton rb1, rb2, rb3, rb4;
    private CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15, cb16;
    private Button submit;
    String email;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_form);

        //Spinner (Dropdown Menu)
        spinner = findViewById(R.id.spinnerConsult);

        //Edit Texts
        et1 = findViewById(R.id.preventionInput);
        et2 = findViewById(R.id.damagesTextInput);
        et3 = findViewById(R.id.petQuestionAnswer);
        et4 = findViewById(R.id.concernsQuestionAnswer);

        //Radio Groups
        rd1 = findViewById(R.id.radioGroup);
        rd2 = findViewById(R.id.radioGroup2);
        rd3 = findViewById(R.id.radioGroup3);
        rd4 = findViewById(R.id.radioGroup4);

        //Button
        submit = findViewById(R.id.btnSubmit);

        //Checkboxes
        cb1 = findViewById(R.id.plastic);
        cb2 = findViewById(R.id.cement);
        cb3 = findViewById(R.id.wood);
        cb4 = findViewById(R.id.rat);
        cb5 = findViewById(R.id.mice);
        cb6 = findViewById(R.id.wasps);
        cb7 = findViewById(R.id.flies);
        cb8 = findViewById(R.id.ants);
        cb9 = findViewById(R.id.bedBugs);
        cb10 = findViewById(R.id.termites);
        cb11 = findViewById(R.id.cockroaches);
        cb12 = findViewById(R.id.mosquitoes);
        cb13 = findViewById(R.id.windows);
        cb14 = findViewById(R.id.doors);
        cb15 = findViewById(R.id.crevices);
        cb16 = findViewById(R.id.vents);

        //Codes to make dropdown menu appear
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.consultDropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        getEmail();

        //Certain condition for Pets
        rd4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.petsYes:
                        et3.setEnabled(true);
                        et3.setFocusable(true);
                        et3.setFocusableInTouchMode(true);
                        break;

                    case R.id.petsNo:
                        et3.setEnabled(false);
                        et3.setFocusable(false);
                        et3.setFocusableInTouchMode(false);
                        break;
                }
            }
        });

    }

    private void getEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            email = user.getEmail();
        }
    }

    public void storeData(View view) {

        try {

            //First text input
            String et1 = this.et1.getText().toString();

            if (et1.isEmpty()) {
                Map<String, Object> input1 = new HashMap<>();
                input1.put("cons_input1", "No Preventions Tried");

                db.collection("_users").document(email).set(input1, SetOptions.merge());
            } else {
                Map<String, Object> input1 = new HashMap<>();
                input1.put("cons_input1", et1);

                db.collection("_users").document(email).set(input1, SetOptions.merge());
            }

            //First radio group
            int radioID = rd1.getCheckedRadioButtonId();
            rb1 = findViewById(radioID);

            if (radioID == -1) {
                Toast.makeText(this, "Please select Days/Weeks/Months", Toast.LENGTH_SHORT).show();
            } else {
                if (radioID == R.id.days) {
                    Map<String, Boolean> input2 = new HashMap<>();
                    input2.put("rd1", true);

                    db.collection("_users").document(email).set(input2, SetOptions.merge());
                } else {
                    Map<String, Boolean> input2 = new HashMap<>();
                    input2.put("rd1", false);

                    db.collection("_users").document(email).set(input2, SetOptions.merge());
                }
                if (radioID == R.id.weeks) {
                    Map<String, Boolean> input2 = new HashMap<>();
                    input2.put("rd2", true);

                    db.collection("_users").document(email).set(input2, SetOptions.merge());
                } else {
                    Map<String, Boolean> input2 = new HashMap<>();
                    input2.put("rd2", false);

                    db.collection("_users").document(email).set(input2, SetOptions.merge());
                }
                if (radioID == R.id.months) {
                    Map<String, Boolean> input2 = new HashMap<>();
                    input2.put("rd3", true);

                    db.collection("_users").document(email).set(input2, SetOptions.merge());
                } else {
                    Map<String, Boolean> input2 = new HashMap<>();
                    input2.put("rd3", false);

                    db.collection("_users").document(email).set(input2, SetOptions.merge());
                }
            }

            //Material Type
            if (cb1.isChecked()) {
                Map<String, Boolean> input3 = new HashMap<>();
                input3.put("chkbx1", true);

                db.collection("_users").document(email).set(input3, SetOptions.merge());
            } else {
                Map<String, Boolean> input3 = new HashMap<>();
                input3.put("chkbx1", false);

                db.collection("_users").document(email).set(input3, SetOptions.merge());
            }
            if (cb2.isChecked()) {
                Map<String, Boolean> input3 = new HashMap<>();
                input3.put("chkbx2", true);

                db.collection("_users").document(email).set(input3, SetOptions.merge());
            } else {
                Map<String, Boolean> input3 = new HashMap<>();
                input3.put("chkbx2", false);

                db.collection("_users").document(email).set(input3, SetOptions.merge());
            }
            if (cb3.isChecked()) {
                Map<String, Boolean> input3 = new HashMap<>();
                input3.put("chkbx3", true);

                db.collection("_users").document(email).set(input3, SetOptions.merge());
            } else {
                Map<String, Boolean> input3 = new HashMap<>();
                input3.put("chkbx3", false);

                db.collection("_users").document(email).set(input3, SetOptions.merge());
            }
            if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()) {
                Toast.makeText(this, "Please select at least one House Material Type", Toast.LENGTH_SHORT).show();
            }

            //House Size
            String itemSelected = spinner.getSelectedItem().toString();

            Map<String, Object> input4 = new HashMap<>();
            input4.put("dropdown1", itemSelected);

            db.collection("_users").document(email).set(input4, SetOptions.merge());


            //Vermin Type
            if (cb4.isChecked()) {
                Map<String, Boolean> input5 = new HashMap<>();
                input5.put("chkbx4", true);

                db.collection("_users").document(email).set(input5, SetOptions.merge());
            } else {
                Map<String, Boolean> input5 = new HashMap<>();
                input5.put("chkbx4", false);

                db.collection("_users").document(email).set(input5, SetOptions.merge());
            }
            if (cb5.isChecked()) {
                Map<String, Boolean> input6 = new HashMap<>();
                input6.put("chkbx5", true);

                db.collection("_users").document(email).set(input6, SetOptions.merge());
            } else {
                Map<String, Boolean> input6 = new HashMap<>();
                input6.put("chkbx5", false);

                db.collection("_users").document(email).set(input6, SetOptions.merge());
            }
            if (cb6.isChecked()) {
                Map<String, Boolean> input7 = new HashMap<>();
                input7.put("chkbx6", true);

                db.collection("_users").document(email).set(input7, SetOptions.merge());
            } else {
                Map<String, Boolean> input7 = new HashMap<>();
                input7.put("chkbx6", false);

                db.collection("_users").document(email).set(input7, SetOptions.merge());
            }
            if (cb7.isChecked()) {
                Map<String, Boolean> input8 = new HashMap<>();
                input8.put("chkbx7", true);

                db.collection("_users").document(email).set(input8, SetOptions.merge());
            } else {
                Map<String, Boolean> input8 = new HashMap<>();
                input8.put("chkbx7", false);

                db.collection("_users").document(email).set(input8, SetOptions.merge());
            }
            if (cb8.isChecked()) {
                Map<String, Boolean> input9 = new HashMap<>();
                input9.put("chkbx8", true);

                db.collection("_users").document(email).set(input9, SetOptions.merge());
            } else {
                Map<String, Boolean> input9 = new HashMap<>();
                input9.put("chkbx8", false);

                db.collection("_users").document(email).set(input9, SetOptions.merge());
            }
            if (cb9.isChecked()) {
                Map<String, Boolean> input10 = new HashMap<>();
                input10.put("chkbx9", true);

                db.collection("_users").document(email).set(input10, SetOptions.merge());
            } else {
                Map<String, Boolean> input10 = new HashMap<>();
                input10.put("chkbx9", false);

                db.collection("_users").document(email).set(input10, SetOptions.merge());
            }
            if (cb10.isChecked()) {
                Map<String, Boolean> input11 = new HashMap<>();
                input11.put("chkbx10", true);

                db.collection("_users").document(email).set(input11, SetOptions.merge());
            } else {
                Map<String, Boolean> input11 = new HashMap<>();
                input11.put("chkbx10", false);

                db.collection("_users").document(email).set(input11, SetOptions.merge());
            }
            if (cb11.isChecked()) {
                Map<String, Boolean> input12 = new HashMap<>();
                input12.put("chkbx11", true);

                db.collection("_users").document(email).set(input12, SetOptions.merge());
            } else {
                Map<String, Boolean> input12 = new HashMap<>();
                input12.put("chkbx11", false);

                db.collection("_users").document(email).set(input12, SetOptions.merge());
            }
            if (cb12.isChecked()) {
                Map<String, Boolean> input13 = new HashMap<>();
                input13.put("chkbx12", true);

                db.collection("_users").document(email).set(input13, SetOptions.merge());
            } else {
                Map<String, Boolean> input13 = new HashMap<>();
                input13.put("chkbx12", false);

                db.collection("_users").document(email).set(input13, SetOptions.merge());
            }
            if (!cb4.isChecked() && !cb5.isChecked() && !cb6.isChecked() && !cb7.isChecked() && !cb8.isChecked() && !cb9.isChecked() && !cb10.isChecked() && !cb11.isChecked() && !cb12.isChecked()) {
                Toast.makeText(this, "Please select at least one Vermin", Toast.LENGTH_SHORT).show();
            }

            //Second text input
            String et2;

            et2 = this.et2.getText().toString();

            Map<String, Object> input14 = new HashMap<>();
            if (et2.isEmpty()) {
                input14.put("cons_input2", "No Damages");

            } else {
                input14.put("cons_input2", et2);

            }
            db.collection("_users").document(email).set(input14, SetOptions.merge());

            //Entry Points
            if (cb13.isChecked()) {
                Map<String, Boolean> input15 = new HashMap<>();
                input15.put("chkbx13", true);

                db.collection("_users").document(email).set(input15, SetOptions.merge());
            } else {
                Map<String, Boolean> input15 = new HashMap<>();
                input15.put("chkbx13", false);

                db.collection("_users").document(email).set(input15, SetOptions.merge());
            }
            if (cb14.isChecked()) {
                Map<String, Boolean> input16 = new HashMap<>();
                input16.put("chkbx14", true);

                db.collection("_users").document(email).set(input16, SetOptions.merge());
            } else {
                Map<String, Boolean> input16 = new HashMap<>();
                input16.put("chkbx14", false);

                db.collection("_users").document(email).set(input16, SetOptions.merge());
            }
            if (cb15.isChecked()) {
                Map<String, Boolean> input17 = new HashMap<>();
                input17.put("chkbx15", true);

                db.collection("_users").document(email).set(input17, SetOptions.merge());
            } else {
                Map<String, Boolean> input17 = new HashMap<>();
                input17.put("chkbx15", false);

                db.collection("_users").document(email).set(input17, SetOptions.merge());
            }
            if (cb16.isChecked()) {
                Map<String, Boolean> input18 = new HashMap<>();
                input18.put("chkbx16", true);

                db.collection("_users").document(email).set(input18, SetOptions.merge());
            } else {
                Map<String, Boolean> input18 = new HashMap<>();
                input18.put("chkbx16", false);

                db.collection("_users").document(email).set(input18, SetOptions.merge());
            }
            if (!cb13.isChecked() && !cb14.isChecked() && !cb15.isChecked() && !cb16.isChecked()) {
                Toast.makeText(this, "Please select at least one Entry Point", Toast.LENGTH_SHORT).show();
            }

            //Relatives in the house?
            int radioID2 = rd2.getCheckedRadioButtonId();
            rb2 = findViewById(radioID2);

            if (radioID2 == -1) {
                Toast.makeText(this, "Please provide an answer for: Relatives in the house", Toast.LENGTH_SHORT).show();
            } else {
                if (radioID2 == R.id.relativeYes) {
                    Map<String, Boolean> input19 = new HashMap<>();
                    input19.put("rd4", true);

                    db.collection("_users").document(email).set(input19, SetOptions.merge());
                } else {
                    Map<String, Boolean> input19 = new HashMap<>();
                    input19.put("rd4", false);

                    db.collection("_users").document(email).set(input19, SetOptions.merge());
                }
                if (radioID2 == R.id.relativeNo) {
                    Map<String, Boolean> input20 = new HashMap<>();
                    input20.put("rd5", true);

                    db.collection("_users").document(email).set(input20, SetOptions.merge());
                } else {
                    Map<String, Boolean> input20 = new HashMap<>();
                    input20.put("rd5", false);

                    db.collection("_users").document(email).set(input20, SetOptions.merge());
                }
            }

            //Children?
            int radioID3 = rd3.getCheckedRadioButtonId();
            rb3 = findViewById(radioID3);

            if (radioID3 == -1) {
                Toast.makeText(this, "Please provide an answer for: Children", Toast.LENGTH_SHORT).show();
            } else {
                if (radioID3 == R.id.childrenYes) {
                    Map<String, Boolean> input21 = new HashMap<>();
                    input21.put("rd6", true);

                    db.collection("_users").document(email).set(input21, SetOptions.merge());
                } else {
                    Map<String, Boolean> input21 = new HashMap<>();
                    input21.put("rd6", false);

                    db.collection("_users").document(email).set(input21, SetOptions.merge());
                }
                if (radioID3 == R.id.childrenNo) {
                    Map<String, Boolean> input22 = new HashMap<>();
                    input22.put("rd7", true);

                    db.collection("_users").document(email).set(input22, SetOptions.merge());
                } else {
                    Map<String, Boolean> input22 = new HashMap<>();
                    input22.put("rd7", false);

                    db.collection("_users").document(email).set(input22, SetOptions.merge());
                }
            }

            //Pets?
            int radioID4 = rd4.getCheckedRadioButtonId();
            rb4 = findViewById(radioID4);
            if (radioID4 == -1) {
                Toast.makeText(this, "Please provide an answer for: Pets", Toast.LENGTH_SHORT).show();
            } else {
                if (radioID4 == R.id.petsYes) {
                    et3.setEnabled(true);
                    et3.setFocusable(true);
                    et3.setFocusableInTouchMode(true);
                    Map<String, Boolean> input23 = new HashMap<>();
                    input23.put("rd8", true);

                    db.collection("_users").document(email).set(input23, SetOptions.merge());
                } else {
                    Map<String, Boolean> input23 = new HashMap<>();
                    input23.put("rd8", false);

                    db.collection("_users").document(email).set(input23, SetOptions.merge());
                }
                if (radioID4 == R.id.petsNo) {
                    Map<String, Boolean> input24 = new HashMap<>();
                    input24.put("rd9", true);

                    db.collection("_users").document(email).set(input24, SetOptions.merge());
                } else {
                    Map<String, Boolean> input24 = new HashMap<>();
                    input24.put("rd9", false);

                    db.collection("_users").document(email).set(input24, SetOptions.merge());
                }
            }

            //If yes, what pet/s
            String et3;

            et3 = this.et3.getText().toString();

            if (et3.isEmpty()) {
                Map<String, Object> input25 = new HashMap<>();
                input25.put("cons_input3", "No Pets");

                db.collection("_users").document(email).set(input25, SetOptions.merge());
            } else {
                Map<String, Object> input25 = new HashMap<>();
                input25.put("cons_input3", et3);

                db.collection("_users").document(email).set(input25, SetOptions.merge());
            }


            //Any more concerns
            String et4;

            et4 = this.et4.getText().toString();

            if (et4.isEmpty()) {
                Map<String, Object> input26 = new HashMap<>();
                input26.put("cons_input4", "No More Concerns");

                db.collection("_users").document(email).set(input26, SetOptions.merge());
            } else {
                Map<String, Object> input26 = new HashMap<>();
                input26.put("cons_input4", et4);

                db.collection("_users").document(email).set(input26, SetOptions.merge());
            }

            if (!(radioID == -1) && !(radioID2 == -1) && !(radioID3 == -1) && !(radioID4 == -1)) {
                if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked()) {
                    if (cb4.isChecked() || cb5.isChecked() || cb6.isChecked() || cb7.isChecked() || cb8.isChecked() || cb9.isChecked() || cb10.isChecked() || cb11.isChecked() || cb12.isChecked()) {
                        if (cb13.isChecked() || cb14.isChecked() || cb15.isChecked() || cb16.isChecked()) {
                            //Add Estimate Field
                            Map<String, Object> input27 = new HashMap<>();
                            input27.put("adminEstimate", "Waiting for Estimate");

                            db.collection("_users").document(email).set(input27, SetOptions.merge());

                            //Add Notes Field
                            Map<String, Object> input28 = new HashMap<>();
                            input28.put("adminNotes", "");
                            db.collection("_users").document(email).set(input28, SetOptions.merge());

                            Toast.makeText(this, "Form Submitted", Toast.LENGTH_SHORT).show();

                            Map<String, Boolean> hasSubmitted = new HashMap<>();
                            hasSubmitted.put("_hasSubmitted", true);
                            hasSubmitted.put("isReviewed", false);

                            db.collection("_users").document(email).set(hasSubmitted, SetOptions.merge());

                            startActivity(new Intent(ConsultationForm.this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.d("ConsultError", e.toString());
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}