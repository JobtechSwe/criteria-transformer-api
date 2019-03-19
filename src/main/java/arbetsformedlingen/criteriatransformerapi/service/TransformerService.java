package arbetsformedlingen.criteriatransformerapi.service;

import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Criteria.GeographicCoordinate;
import arbetsformedlingen.criteriatransformerapi.criteria.Parttime;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.MatchningsparametrarDTO;
import arbetsformedlingen.criteriatransformerapi.elisecriteria.ProfilKriteriumDTO;
import arbetsformedlingen.criteriatransformerapi.exception.ContentNotAllowedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static arbetsformedlingen.criteriatransformerapi.criteria.CriteriaTypeValue.*;
import static arbetsformedlingen.criteriatransformerapi.elisecriteria.Constants.*;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class TransformerService implements ITransformerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Mono<Criteria> transformToCriteria(MatchningsparametrarDTO param) {
        try {
            return transform(param);
        } catch (Exception e) {
            String message = String.format("could not transform param: %s", param);
            throw new ContentNotAllowedException(message, e);
        }
    }

    @Override
    public Mono<Criteria> transform(MatchningsparametrarDTO param) {
        Criteria criteria = new Criteria();
        criteria.setLimit(populateValue(param.getMaxAntal()));
        criteria.setOffset(populateValue(param.getStartrad()));
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
                formattedDate = df.format(date);
            } catch (Exception e) {
                logger.warn(String.format("Could not populate date: {}", date));
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
        String typ = kriteriumDTO.getTyp();
        String varde = kriteriumDTO.getVarde();
        if (KOMMUN.equalsIgnoreCase(typ)) {
            criteria.getMunicipality().add(varde);
        }
        if (LAN.equalsIgnoreCase(typ)) {
            criteria.getRegion().add(varde);
        }
        if (LAND.equalsIgnoreCase(typ)) {
            criteria.getCountry().add(varde);
        }
        if (GEOADRESS.equalsIgnoreCase(typ)) {
            addGeoInfo(criteria, kriteriumDTO);
        }
        if (KOMPETENS.equalsIgnoreCase(typ)) {
            criteria.getSkill().add(varde);
        }
        if (YRKESROLL.equalsIgnoreCase(typ)) {
            criteria.getOccupation().add(varde);
        }
        if (YRKESOMRADE_ROLL.equalsIgnoreCase(typ)) {
            criteria.getField().add(varde);
        }
        if (YRKESGRUPP_ROLL.equalsIgnoreCase(typ)) {
            criteria.getGroup().add(varde);
        }
        if (FRITEXT.equalsIgnoreCase(typ)) {
            populateFritext(criteria, varde);
        }
        if (KORKORT.equalsIgnoreCase(typ)) {
            criteria.getDrivinglicence().add(varde);
        }
        if (INGEN_EFTERFRAGAD_YRKESERFARENHET.equalsIgnoreCase(typ)) {
            if ("1".equals(varde)) {
                criteria.setExperience(false);
            }
        }
        if (ANSTALLNINGSTYP.equalsIgnoreCase(typ)) {
            addAnstallningstyp(criteria, varde);
        }
        if (ARBETSOMFATTNING.equalsIgnoreCase(typ)) {
            if (ENDAST_HELTID.equalsIgnoreCase(varde)) {
                criteria.getExtent().add(VALUE_HELTID);
            } else if (varde.startsWith(DELTID)) {
                criteria.getExtent().add(VALUE_DELTID);
                criteria.setParttime(populateParttime(kriteriumDTO));
            } else if (varde.startsWith(HELTID_DELTID)) {
                criteria.getExtent().add(VALUE_HELTID);
                criteria.getExtent().add(VALUE_DELTID);
                criteria.setParttime(populateParttime(kriteriumDTO));
            }
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
            logger.debug(String.format("could not populate parttime: %s", kriteriumDTO));
        }

        return parttime;
    }

    protected void populateFritext(Criteria criteria, String varde) {
        if (isEmpty(varde)) {
            logger.debug("could not populate fritext when value is null or empty");
            return;
        } else if (isEmpty(criteria.getQ())) {
            criteria.setQ(varde);
        } else {
            criteria.setQ(criteria.getQ() + " " + varde);
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
                    logger.debug("could not handle sort on value: {}", sorteringsordning);
                    return null;
            }
        }
    }

    private String populateValue(Integer value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            logger.debug("could not populate value: {}", value);
        }

        return null;
    }

}
