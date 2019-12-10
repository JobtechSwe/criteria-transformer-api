package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Criteria.GeographicCoordinate;
import arbetsformedlingen.criteriatransformerapi.criteria.Parttime;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.ProfilKriteriumDTO;
import arbetsformedlingen.criteriatransformerapi.exception.ContentNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static arbetsformedlingen.criteriatransformerapi.criteria.CriteriaTypeValue.*;
import static arbetsformedlingen.criteriatransformerapi.elisecriteria.Constants.*;
import static org.springframework.util.StringUtils.isEmpty;

@Service
@Slf4j
public class TransformerService implements ITransformerService {

    private static final Logger logger = LoggerFactory.getLogger(TransformerService.class);
    public static final String WILDCARD_FRITEXT = "**";
    public static final String EMPTY = "";
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Mono<Criteria> transformToCriteria(MatchningsparametrarDTO param) {
        try {
            return transform(param);
        } catch (RuntimeException e) {
            String message = String.format("could not transform param: %s", param);
            throw new ContentNotAllowedException(message, e);
        }
    }

    @Override
    public Mono<Criteria> transform(MatchningsparametrarDTO param) {
        Criteria criteria = new Criteria();
        criteria.setLimit(param.getMaxAntal());
        criteria.setOffset(param.getStartrad());
        criteria.setSort(populateSort(param));
        criteria.setPublishedBefore(populateDate(param.getTillPubliceringsdatum()));
        criteria.setPublishedAfter(populateDate(param.getFranPubliceringsdatum()));

        populateProfilkriterier(criteria, param);

        logger.debug("transformed input to: {} ", criteria);
        return Mono.just(criteria);
    }

    protected String populateDate(Date date) {
        String formattedDate = null;
        if (date != null) {
            try {
                log.info("date: " + date);
                df.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
                formattedDate = df.format(date);
                log.info("formatted-date: " + formattedDate);
            } catch (Exception e) {
                String message = String.format("Could not populate date: %s", date);
                logger.warn(message, e);
            }
        }

        return formattedDate;
    }


    private void populateProfilkriterier(Criteria criteria, MatchningsparametrarDTO matchningsparametrarDTO) {
        if (matchningsparametrarDTO.getMatchningsprofil() != null) {
            matchningsparametrarDTO.getMatchningsprofil().getProfilkriterier()
                    .forEach(dto -> populateKriteria(criteria, dto));
        }
    }

    private void populateKriteria(Criteria criteria, ProfilKriteriumDTO kriteriumDTO) {
        String type = kriteriumDTO.getTyp();
        String value = kriteriumDTO.getVarde();
        if (!isEmpty(type) && !isEmpty(value)) {
            switch (type) {
                case KOMMUN:
                    criteria.getMunicipality().add(value);
                    break;
                case LAN:
                    criteria.getRegion().add(value);
                    break;
                case LAND:
                    criteria.getCountry().add(value);
                    break;
                case GEOADRESS:
                    addGeoInfo(criteria, kriteriumDTO);
                    break;
                case KOMPETENS:
                    criteria.getSkill().add(value);
                    break;
                case YRKE:
                case YRKESROLL:
                    criteria.getOccupation().add(value);
                    break;
                case YRKESOMRADE_ROLL:
                    criteria.getField().add(value);
                    break;
                case YRKESGRUPP_ROLL:
                    criteria.getGroup().add(value);
                    break;
                case SPRAK:
                    criteria.getLanguage().add(value);
                    break;
                case FRITEXT:
                    populateFritext(criteria, value);
                    break;
                case KORKORT:
                    criteria.getDrivinglicence().add(value);
                    break;
                case INGEN_EFTERFRAGAD_YRKESERFARENHET:
                    populateExperience(criteria, value);
                    break;
                case ANSTALLNINGSTYP:
                    addAnstallningstyp(criteria, value);
                    break;
                case ARBETSOMFATTNING:
                    populateExtent(criteria, value);
                    break;
                default:
                    logger.error("could not handle criteria type '{}'", type);
            }
        } else if (ARBETSOMFATTNING.equalsIgnoreCase(type)) {
            try {
                criteria.setParttime(populateParttime(kriteriumDTO));
                if (criteria.getParttime().getMin() < 100 && criteria.getParttime().getMin() > 0 &&
                        criteria.getParttime().getMax() < 100 && criteria.getParttime().getMax() > 0) {
                    criteria.getExtent().add(VALUE_DELTID);
                    //NEED TO NULL THIS OUT BECAUSE DATA FROM SOURCE USES ONLY EXTENT VALUE
                    criteria.setParttime(null);
                }
            } catch (Exception e) {
                String message = String.format("Could not populate arbetsomfattning for criteria: %s", kriteriumDTO);
                logger.warn(message, e);
            }
        }
    }

    private void populateExtent(Criteria criteria, String value) {
        if (ENDAST_HELTID.equalsIgnoreCase(value)) {
            criteria.getExtent().add(VALUE_HELTID);
        } else if (value.startsWith(DELTID)) {
            criteria.getExtent().add(VALUE_DELTID);
        } else if (value.startsWith(HELTID_DELTID)) {
            // No extent is added since this is the same query as "get all ads"
        }
    }


    private void populateExperience(Criteria criteria, String value) {
        if ("1".equals(value)) {
            criteria.setExperience(false);
        }
    }

    protected Parttime populateParttime(ProfilKriteriumDTO kriteriumDTO) {
        Parttime parttime = new Parttime();
        try {
            kriteriumDTO.getEgenskaper().forEach(egenskap -> {
                String egenskapTyp = egenskap.getTyp();
                if (ARBETSOMFATTNING_MIN.equalsIgnoreCase(egenskapTyp)) {
                    parttime.setMin(Integer.valueOf(egenskap.getVarde()));
                }
                if (ARBETSOMFATTNING_MAX.equalsIgnoreCase(egenskapTyp)) {
                    parttime.setMax(Integer.valueOf(egenskap.getVarde()));
                }
            });
        } catch (Exception e) {
            String message = String.format("could not populate parttime: %s", kriteriumDTO);
            logger.warn(message, e);
        }

        return parttime;
    }

    protected void populateFritext(Criteria criteria, String value) {
        if (value.equals(WILDCARD_FRITEXT)) {
            value = EMPTY;
        }

        //removes quote around string
        if (value.startsWith("\"") || value.endsWith("\"")) {
            value = value.replaceAll("^\"|\"$", "");
        }

        if (isEmpty(criteria.getQ())) {
            criteria.setQ(value);
        } else {
            criteria.setQ(criteria.getQ() + " " + value);
        }

    }

    protected void addAnstallningstyp(Criteria criteria, String varde) {
        if (FULL_TIME.equals(varde) || SUMMER_JOB.equals(varde) || BEHOVSANSTALLNING.equals(varde) || FOREGIN.equals(varde)) {
            criteria.getEmploymenttype().add(varde);
        }
    }

    private void addGeoInfo(Criteria criteria, ProfilKriteriumDTO kriteriumDTO) {
        GeographicCoordinate coordinates = new GeographicCoordinate();
        kriteriumDTO.getEgenskaper().forEach(coordinate -> {
            if (LATITUD.equalsIgnoreCase(coordinate.getTyp())) {
                coordinates.setLatitude(coordinate.getVarde());
            }
            if (LONGITUD.equalsIgnoreCase(coordinate.getTyp())) {
                coordinates.setLongitude(coordinate.getVarde());
            }
            if (RADIE.equalsIgnoreCase(coordinate.getTyp())) {
                coordinates.setRadius(coordinate.getVarde());
            }
        });
        criteria.getGeographicCoordinates().add(coordinates);
    }

    private String populateSort(MatchningsparametrarDTO matchningsparametrar) {
        String sorteringsordning = matchningsparametrar.getSorteringsordning();
        if (isEmpty(sorteringsordning)) {
            return null;
        } else {
            switch (sorteringsordning) {
                case SORTERINGSORDNING_RELEVANS:
                    return RELEVANCE;
                case SORTERINGSORDNING_DATUM:
                    return PUBDATE_DESC;
                case SORTERINGSORDNING_SISTAANSOKNINGSDATUM:
                    return APPLYDATE_ASC;
                default:
                    logger.warn("could not handle sort on value: {}", sorteringsordning);
                    return null;
            }
        }
    }
}