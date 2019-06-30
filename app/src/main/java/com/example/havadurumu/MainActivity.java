package com.example.havadurumu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String secilenSehir = "";

    private SwipeRefreshLayout refreshLayout;

    private AutoCompleteTextView sehirSecimiText;

    private LinearLayout bilgilerLinearLayout;

    private TextView sehirAdiText;
    private TextView sicaklikText;
    private TextView nemText;
    private TextView bulutText;
    private TextView ruzgarText;

    private ImageView havaDurumuImage;

    private ConstraintLayout arkaPlan;

    private String[] sehirListesi = {
            "Adana",
            "Adıyaman",
            "Ağrı",
            "Amasya",
            "Ankara",
            "Antalya",
            "Artvin",
            "Aydın",
            "Balıkesir",
            "Bilecik",
            "Bingöl",
            "Bitlis",
            "Bolu",
            "Burdur",
            "Bursa",
            "Çanakkale",
            "Çankırı",
            "Çorum",
            "Denizli",
            "Diyarbakır",
            "Edirne",
            "Elazığ",
            "Erzincan",
            "Erzurum",
            "Eskişehir",
            "Gaziantep",
            "Giresun",
            "Gümüşhane",
            "Hakkari",
            "Hatay",
            "Isparta",
            "Mersin",
            "İstanbul",
            "İzmir",
            "Kars",
            "Kastamonu",
            "Kayseri",
            "Kırklareli",
            "Kırşehir",
            "Kocaeli",
            "Konya",
            "Kütahya",
            "Malatya",
            "Manisa",
            "Kahramanmaraş",
            "Mardin",
            "Muğla",
            "Muş",
            "Nevşehir",
            "Niğde",
            "Ordu",
            "Rize",
            "Sakarya",
            "Samsun",
            "Siirt",
            "Sinop",
            "Sivas",
            "Tekirdağ",
            "Tokat",
            "Trabzon",
            "Tunceli",
            "Şanlıurfa",
            "Uşak",
            "Van",
            "Yozgat",
            "Zonguldak",
            "Aksaray",
            "Bayburt",
            "Karaman",
            "Kırıkkale",
            "Batman",
            "Şırnak",
            "Bartın",
            "Ardahan",
            "Iğdır",
            "Yalova",
            "Karabük",
            "Kilis",
            "Osmaniye",
            "Düzce"
    };


    private String BaseUrl;
    private String ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!internetKontrolu()) {
            Toast.makeText(getApplicationContext(), "Lütfen internete bağlı olduğunuzdan emin olup uygulamayı yeniden açın", Toast.LENGTH_LONG).show();
            finish();
        }

        setBaseUrl(getResources().getString(R.string.BASE_URL));
        setApiKey(getResources().getString(R.string.API_KEY));

        arkaPlan = findViewById(R.id.arka_plan);

        bilgilerLinearLayout = findViewById(R.id.bilgiler_linearlayout);

        sehirAdiText = findViewById(R.id.sehir_adi_text);
        sicaklikText = findViewById(R.id.sicaklik_text);
        nemText = findViewById(R.id.nem_text);
        bulutText = findViewById(R.id.bulut_text);
        ruzgarText = findViewById(R.id.ruzgar_text);

        havaDurumuImage = findViewById(R.id.hava_durumu_image);

        sehirSecimiText = findViewById(R.id.sehir_secimi_autocompletetextview);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sehirListesi);
        sehirSecimiText.setAdapter(arrayAdapter);
        sehirSecimiText.setThreshold(0);
        sehirSecimiText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSecilenSehir(sehirSecimiText.getText().toString());
                goruntuGuncellemesi();
                islemiBaslat(getSecilenSehir());

            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goruntuGuncellemesi();
                if (!secilenSehir.equalsIgnoreCase(""))
                    islemiBaslat(getSecilenSehir());
                refreshLayout.setRefreshing(false);

            }
        });




    }

    public void goruntuGuncellemesi(){
        hideKeyboard();
        sehirSecimiText.setText("");
        sehirSecimiText.clearFocus();
    }

    public void islemiBaslat(String gelenSehirAdi){
        if (!internetKontrolu()){
            toastGoster("İnternet bağlantınızı kontrol edip yeniden deneyiniz");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<CityDetails> call = apiClient.getDetailsFromCityName(gelenSehirAdi + ",tr"
                , getApiKey());

        call.enqueue(new Callback<CityDetails>() {
            @Override
            public void onResponse(Call<CityDetails> call, Response<CityDetails> response) {
                if (!response.isSuccessful()){
                    toastGoster("Bir hata oluştu. Yeniden deneyiniz.");
                }
                else{
                   if (response.body() == null)
                       toastGoster("Bir hata oluştu. Yeniden deneyiniz.");
                   else{
                       Sehir sehir = new Sehir(response.body());
                       String mapIconSource = String.valueOf(getResources().getIdentifier(sehir.getIcon_name(), "drawable", getBaseContext().getPackageName()));
                       if (mapIconSource.equalsIgnoreCase("0")){
                           ikonHatasi("İkon yüklenirken bir hata meydana geldi",havaDurumuImage,getResources().getDrawable(R.drawable.ic_error));
                       }
                       else{
                           try {
                               setBackgroundSourceWithSmoothAnimation(havaDurumuImage,(getResources().getDrawable(Integer.parseInt(mapIconSource))));
                           } catch (Resources.NotFoundException e) {
                               ikonHatasi("İkon yüklenirken bir hata meydana geldi",havaDurumuImage,getResources().getDrawable(R.drawable.ic_error));
                           } catch (NumberFormatException e) {
                               ikonHatasi("İkon yüklenirken bir hata meydana geldi",havaDurumuImage,getResources().getDrawable(R.drawable.ic_error));
                           }
                       }

                       setTextWithSmoothAnimation(sehirAdiText,sehir.getAd());
                       setTextWithSmoothAnimation(sicaklikText,sehir.getSicaklik());
                       setTextWithSmoothAnimation(nemText,sehir.getNemOrani());
                       setTextWithSmoothAnimation(bulutText,sehir.getBulutOrani());
                       setTextWithSmoothAnimation(ruzgarText,sehir.getRuzgarHizi());

                       if (bilgilerLinearLayout.getVisibility() == View.GONE )
                           bilgilerLinearLayout.setVisibility(View.VISIBLE);

                       String zaman = sehir.getZaman();
                       switch (zaman){
                           case "gunduz":
                               arkaPlan.setBackgroundColor(getResources().getColor(R.color.gunduz));
                               break;
                           case "gece":
                               arkaPlan.setBackgroundColor(getResources().getColor(R.color.gece));
                               break;
                       }
                   }
                }
            }
            @Override
            public void onFailure(Call<CityDetails> call, Throwable t) {
                toastGoster("Bir hata oluştu. Yeniden deneyiniz.");
            }
        });



    }

    public void toastGoster(String gelenMesaj){
        Toast.makeText(getApplicationContext(),gelenMesaj,Toast.LENGTH_LONG).show();
    }

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = this.getCurrentFocus();
        if (v != null) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void ikonHatasi(String hataMesaji, ImageView hataAlani, Drawable hataResmi){
        toastGoster(hataMesaji);
        setBackgroundSourceWithSmoothAnimation(hataAlani,hataResmi);
    }

    public boolean internetKontrolu(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected;
    }

    private void setTextWithSmoothAnimation(final TextView textView, final String message) {
        textView.animate().setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(message);
                textView.animate().setListener(null).setDuration(300).alpha(1);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        }).alpha(0);
    }

    private void setBackgroundSourceWithSmoothAnimation(final ImageView havaDurumuImage, final Drawable source) {
        havaDurumuImage.animate().setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                havaDurumuImage.setImageDrawable(source);
                havaDurumuImage.animate().setListener(null).setDuration(300).alpha(1);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        }).alpha(0);
    }

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        BaseUrl = baseUrl;
    }

    public String getApiKey() {
        return ApiKey;
    }

    public void setApiKey(String apiKey) {
        ApiKey = apiKey;
    }

    public String getSecilenSehir() {
        return secilenSehir;
    }

    public void setSecilenSehir(String secilenSehir) {
        this.secilenSehir = secilenSehir;
    }

}
