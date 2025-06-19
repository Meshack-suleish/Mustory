package com.mustory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listView;
    private ProgressBar progressBar;

    private ArrayList<Song> songs;
    private MusicAdapter adapter;
    private ExtendedFloatingActionButton listenOnlineFab;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.playlist_recycler_view);
        listenOnlineFab = findViewById(R.id.listen_online_fab);

        songs = new ArrayList<>();
        populateSongs();

        adapter = new MusicAdapter(this, songs, position -> {
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            intent.putParcelableArrayListExtra("songs", new ArrayList<>(songs));
            intent.putExtra("position", position);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listenOnlineFab.setOnClickListener(v -> {
            showListenOnlineDialog();
        });
    }


    private void populateSongs() {
        String path = "android.resource://" + getPackageName() + "/" + R.raw.pillars_of_faith_jua_litue;
        String path1 = "android.resource://" + getPackageName() + "/" + R.raw.u_mwendo_gani_nyumbani;
        String path2 = "android.resource://" + getPackageName() + "/" + R.raw.yesu_kwetu_ni_rafiki;

        songs.add(new Song("Waves", "Chill lofi beat", "DJ Breeze", path, 1));
        songs.add(new Song(
                "U Mwendo Gani Nyumbani",

                "158-U Mwendo Gani Nyumbani? (How Far from Home?)\n" +
                        "\n" +
                        "U mwendo gani nyumbani? Mlinzi akanijibu,\n" +
                        "\"Usiku sasa waisha, machweo karibu.\"\n" +
                        "Usihuzunike tena, bali ulemee mwendo\n" +
                        "Hata ushike, ufalme kule mwangani juu.\n" +
                        "\n" +
                        "Na tena niliuliza, nchi yote ikajibau:\n" +
                        "\"Sasa mwendo watimika, milele karibu.\"\n" +
                        "Usihuzu-nike tena, ishara kuu zasonga\n" +
                        "Na viumbe vyangojea sauti ya Bwana.\n" +
                        "\n" +
                        "Nikamwuliza shujaa, ndivyo kanitia moyo:\n" +
                        "\"Shikilia mapigano, kitambo yaisha.\"\n" +
                        "Usihuzunike tena, kazi ifanye kwa moyo;\n" +
                        "Tumeahidiwa tunu tuishapo shinda.\n" +
                        "\n" +

                        "Siyo mbali na nyumbani! fikara tamu njiani,\n " +
                        "Latupoza roho nalo, lafuta machozi. \n" +
                        "Usihuzunike tena, kitambo tutakutana \n" +
                        "Wenye furaha kamili nyumbani mwa Baba.\n" +
                        "\n" +


                        " Historia\n" +
                        "\n" +
                        "Wimbo huu uliandikwa mnamo mwaka 1853 na Annie Rebekah Smith, kisha ukaingizwa katika gazeti la \"Review and Herald\" " +
                        "mnamo mwezi wa 11, tarehe 29, mwaka 1853. Anie Smith, alikuwa ni binti pekee wa Samweli na Rebekah Spalding Smith ambaye " +
                        "alizaliwa magharibi mwa Wilton-New Hampshire katika mwaka wa 1828, tarehe 16 ya mwezi wa 3. Alijiunga na kanisa la Baptist mnamo " +
                        "mwaka 1838, kisha kuwa mfuasi wa Willium Miller. Baada ya kukatishwa tamaa kutokana na kutokuja kwa Yesu Kristo mara ya pili katika mwaka 1844 " +
                        "mwezi wa 10 (2nd great dissapointment), alipoteza imani juu ya fundisho la ujio wa Yesu mara ya pili na akaachana na masuala ya injili, kisha" +
                        " akaanza kupambana na maisha ya kila siku. Alisomea ualimu, na baadae pia akajihusisha na maswala ya sanaa- upakaji rangi (Painting).\n" +
                        "\n" +
                        "Katika mwaka wa 1851 akiwa Charletown- Massachussetts, alipokea barua kutoka kwa mama yake ikimualika kuhudhuria mahubiri yaliyokuwa " +
                        "yakiendeshwa na Joseph Bates huko Somerville, kilomita 2 kutoka pale alipokuwa akiishi. Bila kusita, Annie aliitikia wito na kukubali kwenda, akiwa na lengo" +
                        " la kumfurahisha mama yake. Usiku wa kuamkia siku aliyopanga kwenda mkutanoni, alipokuwa amelala aliota ndoto iliyoelezea kwamba: ameenda mkutanoni akiwa amechelewa, " +
                        "kisha akakaa karibu na mlango akisikiliza somo juu ya unabii wa siku 2300 wa Daniel 8:14. Hivyo nyakati za jioni katika siku hiyo husika alianza safari mapema kutoka " +
                        "nyumbani kuelekea mkutanoni; Kwa bahati mbaya, akiwa njiani, alipotea njia kisha kufika mkutanoni akiwa amechelewa; na yote aliyoyaota yalititimia. Pia, Muhubiri( Joseph Bates)" +
                        " naye aliota ndoto kama ile ya Annie nayo ikatitimia kama alivyoota." +
                        "Baada ya Annie kuketi mlangoni akisikiliza mahubiri juu ya Danieli 8:14, aliguswa sana na akaamua kujitoa upya kwa Mungu na kusalimisha kipawa chake cha utunzi wa mashairi na" +
                        " uandishi wa magazeti katika kulisaidia kanisa. Kutokana na ujumbe huo alioupata uthibitisho wa uhakika juu ya ujio wa Yesu mara ya pili, inasemekana kuwa ndivyo vilivyo mfanya kuandika wimbo huu " +
                        "\"Umwendo gani nyumbani?\". Haikupita muda mrefu tangu amejitoa, Annie akapata mambukizi ya TB (Tuberculosis) kisha kufariki katika mwaka wa 1855, tarehe 26 ya mwezi wa 7. huko Wilton- New Hampshire, " +
                        "akiwa na umri wa miaka 27. Kwa kipindi hicho alikuwa ametumika si zaidi ya miaka 4 katika kanisa, na alikuwa ameandika nyimbo nyingi ambazo zilileta mvuto wa kudumu kwa waumini waliojenga msingi wa Kanisa la Waadventista Wasabato.",


                 "Annie Smith",
                       path1,
                158
        ));

        songs.add(new Song(
                "Yesu Kwetu Ni Rafiki",
                "130-Yesu Kwetu Ni Rafiki (What A Friend We have in Jesus)\n" +
                        "\n" +
                        "Yesu kwetu ni Rafiki, hwambiwa haja pia;\n" +
                        "Tukiomba kwa Babaye, maombi asikia;\n" +
                        "Lakini twajikosesha, twajitweka vibaya;\n" +
                        "Kama tulimwomba Mungu, dua atasikia.\n" +
                        "\n" +
                        "Unadhiki na maonjo? Unamashaka pia?\n" +
                        "Haifai kufa moyo, dua atasikia.\n" +
                        "Hakuna mwingine Mwema, wakutuhurumia;\n" +
                        "Atujua tu dhaifu; maombi asikia.\n" +
                        "\n" +
                        "Je, hunayo hata nguvu, huwezi kwendelea,\n" +
                        "Ujapodharauliwa, ujaporushwa pia.\n" +
                        "Watu wakikudharau, wapendao dunia.\n" +
                        "Hukwambata mikononi, dua atasikia\n" +
                        "\n" +
                        "Historia\n" +
                        "\n" +
                        "Maisha ya ulimwengu huu yamejaa changamoto nyingi sana, na kila mmoja wetu anauzoefu wake " +
                        "katika changamoto ambao ni tofauti na mtu mwingine. Mtunzi wa wimbo huu (Joseph Medlicott Scriven) " +
                        "ni miongoni mwa watu waliopitia changamoto za ajabu katika maisha. Alizaliwa mnamo mwaka 1819, tarehe 10" +
                        " ya mwezi wa 9, maeneo ya Seapatrick-Ireland na kusoma katika chuo cha Trinity huko Dublin-Ulaya. Kabla ya" +
                        " kuhitimu mafunzo chuoni, alijiunga na jeshi. Lakini kutokana na afya yake kuwa mbaya (Isiyoweza kuhimili mikiki ya jeshi), " +
                        "aliamua kurudi tena chuoni kuendelea na masomo hadi alipohitimu shahada yake ya" +
                        "ualimu(B.A). Historia inaeleza kuwa, Joseph alimpenda binti mmoja na kuadhimu kufunga naye ndoa katika mwaka wa 1840, lakini" +
                        " kwa bahati mbaya, mchumba wake alirushwa majini na Farasi wake kisha kufa maji kabla ya ndoa kufungwa. Kutokana na tukio hilo" +
                        " lililomuhuzunisha, aliamua kuhamia Kanada mnamo mwaka 1844 na kuanza maisha mapya ya ualimu. Kwa mara nyingine tena alipata mchumba " +
                        "ambaye pia aliadhimu kufunga naye ndoa; lakini kabla ya ndoa yao kufungwa, mchumba wake (wa pili) alifariki kwa (Pneumonia) iliyotokana na kubatizwa katika ziwa lenye barafu.\n" +
                        "\n" +
                        "Ingawa Joseph alikuwa na changamoto za kifedha na msongo wa mawazo kutokana na changamoto zilizompata, bado alitambulika katika " +
                        "mji wa Bewdley -Kanada kama mkristo mwaminifu na mwenye kujali wahitaji" +
                        "Ingawa Joseph alikuwa na changamoto za kifedha na msongo wa mawazo kutokana na changamoto zilizompata, bado alitambulika katika mji wa Bewdley -Kanada kama mkristo mwaminifu na mwenye kujali wahitaji.\n" +
                        "\n" +
                        "Sikummoja, Joseph akiwa katika kazi zake za kila siku, alipokea ujumbe unaoeleza kwamba: mama yake ambaye ni mjane (Jane Medlicott Scriven) kutoka Ireland," +
                        " mwenye umri wa miaka 70 kwa wakati huo, alikuwa mgonjwa sana. Kutokana na changamoto za kifedha, Joseph hakuweza kwenda kumtazama mama yake, hivyo aliamua kumtumia " +
                        "(mama yake) shairi akiwa na tumaini kuwa lingeweza kumfariji na kumrejesha tumaini katika nyakati za kuugua kwake. Shairi hilo aliliandika katika mwaka 1855 likiwa na " +
                        "kichwa kisemacho \"Omba bila kukoma\" na aliandiika pasipotegemea kuwa sikumoja litakuwa wimbo upendwao na wengi. Lakini baada ya muda, Joseph aliugua na rafiki yake akaja kumuhudumia." +
                        " Alipokuwa akimuhudumia aliona karatasi iliyochoka ikiwa chini imeandikwa shairi alilotunga Joseph" +
                        "akaichukua karatasi hiyo yenye shairi, kisha akatengeneza nakala na kuituma kwa wachapishaji wa magazeti ya kidini (religious journal) ambapo shairi hilo liliweza kuchapwa likiwa katika mfumo wa wimbo kamili.\n" +
                        "\n" +
                        "Baada ya misukosuko ya maisha, mnamo mwaka 1866, tarehe 10 ya mwezi wa 10, Joseph alikutwa amefariki katika ziwa la Rice (Rice lake) huko Bewdly- Ontario, na haikujulikana " +
                        "chanzo cha kuzama kwake ziwani na kufariki.",
                "Joseph Scriven",
                path2,
                130

        ));
    }



    private void showListenOnlineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Get more online");
        builder.setMessage("This is a Beta Version, please rate this app to support the developer +255 742 291 391");

        builder.setPositiveButton("OK", (dialog, which) -> {
            Toast.makeText(MainActivity.this, "Stay turned soon to come!...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            Toast.makeText(MainActivity.this, "Cancelled.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
