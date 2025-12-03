package io.horizontalsystems.bankwallet.modules.authentication.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.ILocalStorage
import io.horizontalsystems.bankwallet.core.providers.resource.Country
import io.horizontalsystems.bankwallet.core.providers.resource.CountryData
import io.horizontalsystems.bankwallet.core.providers.resource.CountryFlag
import io.horizontalsystems.bankwallet.core.providers.resource.IResourceProvider
import kotlinx.coroutines.launch


class RegisterActivityModel(
    val localStorage: ILocalStorage?,
    val resourceProvider: IResourceProvider?
) : ViewModel() {
    private var selectedCountryCode: String? = null
    var searchedCountry = mutableStateOf("")
    var currentProgress = mutableFloatStateOf(0.1f)


    val countries = listOf(
        Country("US", "United States"),
        Country("CA", "Canada"),
        Country("MX", "Mexico"),
        Country("BR", "Brazil"),
        Country("AR", "Argentina"),
        Country("CL", "Chile"),
        Country("CO", "Colombia"),
        Country("PE", "Peru"),
        Country("VE", "Venezuela"),
        Country("UY", "Uruguay"),

        Country("GB", "United Kingdom"),
        Country("DE", "Germany"),
        Country("FR", "France"),
        Country("IT", "Italy"),
        Country("ES", "Spain"),
        Country("NL", "Netherlands"),
        Country("BE", "Belgium"),
        Country("CH", "Switzerland"),
        Country("AT", "Austria"),
        Country("SE", "Sweden"),

        Country("NO", "Norway"),
        Country("DK", "Denmark"),
        Country("FI", "Finland"),
        Country("IE", "Ireland"),
        Country("PT", "Portugal"),
        Country("PL", "Poland"),
        Country("CZ", "Czech Republic"),
        Country("HU", "Hungary"),
        Country("RO", "Romania"),
        Country("GR", "Greece"),

        Country("RU", "Russia"),
        Country("TR", "Turkey"),
        Country("UA", "Ukraine"),
        Country("IL", "Israel"),
        Country("SA", "Saudi Arabia"),
        Country("AE", "United Arab Emirates"),
        Country("EG", "Egypt"),
        Country("ZA", "South Africa"),
        Country("NG", "Nigeria"),
        Country("KE", "Kenya"),

        Country("IN", "India"),
        Country("CN", "China"),
        Country("JP", "Japan"),
        Country("KR", "South Korea"),
        Country("PH", "Philippines"),
        Country("ID", "Indonesia"),
        Country("MY", "Malaysia"),
        Country("SG", "Singapore"),
        Country("TH", "Thailand"),
        Country("VN", "Vietnam"),

        Country("AU", "Australia"),
        Country("NZ", "New Zealand")
    )
    companion object {
        val countryFlags = listOf(
            CountryFlag("US", R.drawable.icon_32_flag_usa),
            CountryFlag("CA", R.drawable.icon_32_flag_canada),
            CountryFlag("AF", R.drawable.flag_af),
            CountryFlag("AD", R.drawable.flag_ad),
            CountryFlag("AE", R.drawable.flag_ae),
            CountryFlag("AG", R.drawable.flag_ag),
            CountryFlag("AI", R.drawable.flag_ai),
            CountryFlag("AL", R.drawable.flag_al),
            CountryFlag("AM", R.drawable.flag_am),
            CountryFlag("AO", R.drawable.flag_ao),
            CountryFlag("AR", R.drawable.flag_ar),
            CountryFlag("AS", R.drawable.flag_as),
            CountryFlag("AT", R.drawable.flag_at),
            CountryFlag("AU", R.drawable.flag_au),
            CountryFlag("AW", R.drawable.flag_aw),
            CountryFlag("AZ", R.drawable.flag_az),
            CountryFlag("BB", R.drawable.flag_bb),
            CountryFlag("BD", R.drawable.flag_bd),
            CountryFlag("BE", R.drawable.flag_be),
            CountryFlag("BF", R.drawable.flag_bf),
            CountryFlag("BG", R.drawable.flag_bg),
            CountryFlag("BH", R.drawable.flag_bh),
            CountryFlag("BI", R.drawable.flag_bi),
            CountryFlag("BJ", R.drawable.flag_bj),
            CountryFlag("BL", R.drawable.flag_bl),
            CountryFlag("BM", R.drawable.flag_bm),
            CountryFlag("BN", R.drawable.flag_bn),
            CountryFlag("BO", R.drawable.flag_bo),
            CountryFlag("BR", R.drawable.flag_br),
            CountryFlag("BS", R.drawable.flag_bs),
            CountryFlag("BT", R.drawable.flag_bt),
            CountryFlag("BW", R.drawable.flag_bw),
            CountryFlag("BY", R.drawable.flag_by),
            CountryFlag("BZ", R.drawable.flag_bz),
            CountryFlag("CG", R.drawable.flag_cg),
            CountryFlag("CH", R.drawable.flag_ch),
            CountryFlag("CI", R.drawable.flag_ci),
            CountryFlag("CK", R.drawable.flag_ck),
            CountryFlag("CL", R.drawable.flag_cl),
            CountryFlag("CM", R.drawable.flag_cm),
            CountryFlag("CN", R.drawable.flag_cn),
            CountryFlag("CO", R.drawable.flag_co),
            CountryFlag("CR", R.drawable.flag_cr),
            CountryFlag("CU", R.drawable.flag_cu),
            CountryFlag("CV", R.drawable.flag_cv),
            CountryFlag("CY", R.drawable.flag_cy),
            CountryFlag("CZ", R.drawable.flag_cz),
            CountryFlag("CD", R.drawable.flag_cd),
            CountryFlag("CF", R.drawable.flag_cf),
            CountryFlag("DE", R.drawable.flag_de),
            CountryFlag("DJ", R.drawable.flag_dj),
            CountryFlag("DK", R.drawable.flag_dk),
            CountryFlag("DM", R.drawable.flag_dm),
            CountryFlag("DO", R.drawable.flag_resource_do),
            CountryFlag("DZ", R.drawable.flag_dz),
            CountryFlag("EC", R.drawable.flag_ec),
            CountryFlag("EE", R.drawable.flag_ee),
            CountryFlag("EG", R.drawable.flag_eg),
            CountryFlag("ER", R.drawable.flag_er),
            CountryFlag("ES", R.drawable.flag_es),
            CountryFlag("ET", R.drawable.flag_et),
            CountryFlag("FI", R.drawable.flag_fi),
            CountryFlag("FJ", R.drawable.flag_fj),
            CountryFlag("FK", R.drawable.flag_fk),
            CountryFlag("FO", R.drawable.flag_fo),
            CountryFlag("FR", R.drawable.flag_fr),
            CountryFlag("GA", R.drawable.flag_ga),
            CountryFlag("GB", R.drawable.flag_gb),
            CountryFlag("GD", R.drawable.flag_gd),
            CountryFlag("GE", R.drawable.flag_ge),
            CountryFlag("GF", R.drawable.flag_gf),
            CountryFlag("GH", R.drawable.flag_gh),
            CountryFlag("GI", R.drawable.flag_gi),
            CountryFlag("GL", R.drawable.flag_gl),
            CountryFlag("GM", R.drawable.flag_gm),
            CountryFlag("GN", R.drawable.flag_gn),
            CountryFlag("GP", R.drawable.flag_gp),
            CountryFlag("GQ", R.drawable.flag_gq),
            CountryFlag("GR", R.drawable.flag_gr),
            CountryFlag("GT", R.drawable.flag_gt),
            CountryFlag("GU", R.drawable.flag_gu),
            CountryFlag("GW", R.drawable.flag_gw),
            CountryFlag("GY", R.drawable.flag_gy),
            CountryFlag("HK", R.drawable.flag_hk),
            CountryFlag("HN", R.drawable.flag_hn),
            CountryFlag("HR", R.drawable.flag_hr),
            CountryFlag("HT", R.drawable.flag_ht),
            CountryFlag("HU", R.drawable.flag_hu),
            CountryFlag("ID", R.drawable.flag_id),
            CountryFlag("IE", R.drawable.flag_ie),
            CountryFlag("IL", R.drawable.flag_il),
            CountryFlag("IN", R.drawable.flag_in),
            CountryFlag("IO", R.drawable.flag_io),
            CountryFlag("IQ", R.drawable.flag_iq),
            CountryFlag("IR", R.drawable.flag_ir),
            CountryFlag("IS", R.drawable.flag_is),
            CountryFlag("IT", R.drawable.flag_it),
            CountryFlag("JM", R.drawable.flag_jm),
            CountryFlag("JO", R.drawable.flag_jo),
            CountryFlag("JP", R.drawable.flag_jp),
            CountryFlag("KE", R.drawable.flag_ke),
            CountryFlag("KG", R.drawable.flag_kg),
            CountryFlag("KH", R.drawable.flag_kh),
            CountryFlag("KI", R.drawable.flag_ki),
            CountryFlag("KM", R.drawable.flag_km),
            CountryFlag("KN", R.drawable.flag_kn),
            CountryFlag("KP", R.drawable.flag_kp),
            CountryFlag("KR", R.drawable.flag_kr),
            CountryFlag("KW", R.drawable.flag_kw),
            CountryFlag("KY", R.drawable.flag_ky),
            CountryFlag("KZ", R.drawable.flag_kz),
            CountryFlag("LA", R.drawable.flag_la),
            CountryFlag("LB", R.drawable.flag_lb),
            CountryFlag("LC", R.drawable.flag_lc),
            CountryFlag("LI", R.drawable.flag_li),
            CountryFlag("LK", R.drawable.flag_lk),
            CountryFlag("LR", R.drawable.flag_lr),
            CountryFlag("LS", R.drawable.flag_ls),
            CountryFlag("LT", R.drawable.flag_lt),
            CountryFlag("LU", R.drawable.flag_lu),
            CountryFlag("LV", R.drawable.flag_lv),
            CountryFlag("LY", R.drawable.flag_ly),
            CountryFlag("MA", R.drawable.flag_ma),
            CountryFlag("MC", R.drawable.flag_mc),
            CountryFlag("MD", R.drawable.flag_md),
            CountryFlag("ME", R.drawable.flag_me),
            CountryFlag("MF", R.drawable.flag_mf),
            CountryFlag("MG", R.drawable.flag_mg),
            CountryFlag("MH", R.drawable.flag_mh),
            CountryFlag("MK", R.drawable.flag_mk),
            CountryFlag("ML", R.drawable.flag_ml),
            CountryFlag("MM", R.drawable.flag_mm),
            CountryFlag("MN", R.drawable.flag_mn),
            CountryFlag("MO", R.drawable.flag_mo),
            CountryFlag("MP", R.drawable.flag_mp),
            CountryFlag("MQ", R.drawable.flag_mq),
            CountryFlag("MR", R.drawable.flag_mr),
            CountryFlag("MS", R.drawable.flag_ms),
            CountryFlag("MT", R.drawable.flag_mt),
            CountryFlag("MU", R.drawable.flag_mu),
            CountryFlag("MV", R.drawable.flag_mv),
            CountryFlag("MW", R.drawable.flag_mw),
            CountryFlag("MX", R.drawable.flag_mx),
            CountryFlag("MY", R.drawable.flag_my),
            CountryFlag("MZ", R.drawable.flag_mz),
            CountryFlag("NA", R.drawable.flag_na),
            CountryFlag("NC", R.drawable.flag_nc),
            CountryFlag("NE", R.drawable.flag_ne),
            CountryFlag("NF", R.drawable.flag_nf),
            CountryFlag("NG", R.drawable.flag_ng),
            CountryFlag("NI", R.drawable.flag_ni),
            CountryFlag("NL", R.drawable.flag_nl),
            CountryFlag("NO", R.drawable.flag_no),
            CountryFlag("NP", R.drawable.flag_np),
            CountryFlag("NR", R.drawable.flag_nr),
            CountryFlag("NU", R.drawable.flag_nu),
            CountryFlag("NZ", R.drawable.flag_nz),
            CountryFlag("OM", R.drawable.flag_om),
            CountryFlag("PA", R.drawable.flag_pa),
            CountryFlag("PE", R.drawable.flag_pe),
            CountryFlag("PF", R.drawable.flag_pf),
            CountryFlag("PG", R.drawable.flag_pg),
            CountryFlag("PH", R.drawable.flag_ph),
            CountryFlag("PK", R.drawable.flag_pk),
            CountryFlag("PL", R.drawable.flag_pl),
            CountryFlag("PM", R.drawable.flag_pm),
            CountryFlag("PR", R.drawable.flag_pr),
            CountryFlag("PS", R.drawable.flag_ps),
            CountryFlag("PT", R.drawable.flag_pt),
            CountryFlag("PW", R.drawable.flag_pw),
            CountryFlag("PY", R.drawable.flag_py),
            CountryFlag("QA", R.drawable.flag_qa),
            CountryFlag("RE", R.drawable.flag_re),
            CountryFlag("RO", R.drawable.flag_ro),
            CountryFlag("RS", R.drawable.flag_rs),
            CountryFlag("RU", R.drawable.flag_ru),
            CountryFlag("RW", R.drawable.flag_rw),
            CountryFlag("SA", R.drawable.flag_sa),
            CountryFlag("SB", R.drawable.flag_sb),
            CountryFlag("SC", R.drawable.flag_sc),
            CountryFlag("SD", R.drawable.flag_sd),
            CountryFlag("SE", R.drawable.flag_se),
            CountryFlag("SG", R.drawable.flag_sg),
            CountryFlag("SI", R.drawable.flag_si),
            CountryFlag("SK", R.drawable.flag_sk),
            CountryFlag("SL", R.drawable.flag_sl),
            CountryFlag("SM", R.drawable.flag_sm),
            CountryFlag("SN", R.drawable.flag_sn),
            CountryFlag("SO", R.drawable.flag_so),
            CountryFlag("SR", R.drawable.flag_sr),
            CountryFlag("ST", R.drawable.flag_st),
            CountryFlag("SV", R.drawable.flag_sv),
            CountryFlag("SY", R.drawable.flag_sy),
            CountryFlag("SZ", R.drawable.flag_sz),
            CountryFlag("TC", R.drawable.flag_tc),
            CountryFlag("TG", R.drawable.flag_tg),
            CountryFlag("TH", R.drawable.flag_th),
            CountryFlag("TJ", R.drawable.flag_tj),
            CountryFlag("TK", R.drawable.flag_tk),
            CountryFlag("TL", R.drawable.flag_tl),
            CountryFlag("TM", R.drawable.flag_tm),
            CountryFlag("TN", R.drawable.flag_tn),
            CountryFlag("TO", R.drawable.flag_to),
            CountryFlag("TR", R.drawable.flag_tr),
            CountryFlag("TT", R.drawable.flag_tt),
            CountryFlag("TV", R.drawable.flag_tv),
            CountryFlag("TW", R.drawable.flag_tw),
            CountryFlag("TZ", R.drawable.flag_tz),
            CountryFlag("UA", R.drawable.flag_ua),
            CountryFlag("UG", R.drawable.flag_ug),
            CountryFlag("UY", R.drawable.flag_uy),
            CountryFlag("UZ", R.drawable.flag_uz),
            CountryFlag("VA", R.drawable.flag_va),
            CountryFlag("VC", R.drawable.flag_vc),
            CountryFlag("VE", R.drawable.flag_ve),
            CountryFlag("VG", R.drawable.flag_vg),
            CountryFlag("VI", R.drawable.flag_vi),
            CountryFlag("VN", R.drawable.flag_vn),
            CountryFlag("VU", R.drawable.flag_vu),
            CountryFlag("WF", R.drawable.flag_wf),
            CountryFlag("WS", R.drawable.flag_ws),
            CountryFlag("XK", R.drawable.flag_xk),
            CountryFlag("YE", R.drawable.flag_ye),
            CountryFlag("YT", R.drawable.flag_yt),
            CountryFlag("ZA", R.drawable.flag_za),
            CountryFlag("ZM", R.drawable.flag_zm),
            CountryFlag("ZW", R.drawable.flag_zw)
        )
    }


    var currentCountryCode = mutableStateOf("us")

    val otherCountries = derivedStateOf {
        val data = countryData.value ?: return@derivedStateOf emptyList()
        val exclude = currentCountryCode.value

        data.countries.filter { c ->
            !c.code.contains(exclude, true)
        }
    }

    val filteredCountries = derivedStateOf {
        val data = otherCountries.value
        val search = searchedCountry.value.lowercase()

        if (search.isEmpty() || search == "") return@derivedStateOf data
        else
            data.filter { c ->
                c.name.lowercase().contains(search, true)
            }
    }

    val countryData = viewModelState { loadCountries() }

    fun setCountryCode(code: String) {
        currentCountryCode.value = code
    }

    fun setSearchedCountry(selectedCountry: String) {
        searchedCountry.value = selectedCountry
    }

    fun <T> viewModelState(loader: suspend () -> T): MutableState<T?> {
        val state = mutableStateOf<T?>(null)
        viewModelScope.launch {
            state.value = loader()
        }
        return state
    }

    fun setRegisterProgress(progress: Float) {
        currentProgress.value = progress
    }

    fun setSelectedCountryCode(code: String?) {
        this.selectedCountryCode = code
    }

    suspend fun loadCountries(): CountryData {
        return resourceProvider?.availableCountries() ?: CountryData(countries)
    }
}
