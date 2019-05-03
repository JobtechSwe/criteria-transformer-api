package arbetsformedlingen.criteriatransformerapi.service;


import arbetsformedlingen.criteriatransformerapi.criteria.Criteria;
import arbetsformedlingen.criteriatransformerapi.criteria.Parttime;
import arbetsformedlingen.criteriatransformerapi.exception.QueryCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class QueryCreatorService implements IQueryCreatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCreatorService.class);

    @Override
    public Mono<String> createQueryParamFor(Criteria criteria) {
        try {
            return createQuery(criteria);
        } catch (RuntimeException exception) {
            String message = String.format("could not create query for criteria: %s", criteria);
            throw new QueryCreationException(message, exception);
        }
    }

    @Override
    public Mono<String> createQuery(Criteria criteria) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        String q = criteria.getQ();
        if (isNotEmpty(q)) {
            builder.queryParam("q", q);
        }

        String offset = criteria.getOffset();
        if (isNotEmpty(offset)) {
            builder.queryParam("offset", offset);
        }

        String limit = criteria.getLimit();
        if (isNotEmpty(limit)) {
            builder.queryParam("limit", limit);
        }

        String sort = criteria.getSort();
        if (isNotEmpty(sort)) {
            builder.queryParam("sort", sort);
        }

        String publishedBefore = criteria.getPublishedBefore();
        if (isNotEmpty(publishedBefore)) {
            builder.queryParam("published-before", publishedBefore);
        }

        String publishedAfter = criteria.getPublishedAfter();
        if (isNotEmpty(publishedAfter)) {
            builder.queryParam("published-after", publishedAfter);
        }

        List<String> occupation = criteria.getOccupation();
        if (!isEmpty(occupation)) {
            builder.queryParam("occupation-name", occupation.toArray(new Object[occupation.size()]));
        }

        List<String> group = criteria.getGroup();
        if (!isEmpty(group)) {
            builder.queryParam("occupation-group", group.toArray(new Object[group.size()]));
        }

        List<String> field = criteria.getField();
        if (!isEmpty(field)) {
            builder.queryParam("occupation-field", field.toArray(new Object[field.size()]));
        }

        List<String> skill = criteria.getSkill();
        if (!isEmpty(skill)) {
            builder.queryParam("skill", skill.toArray(new Object[skill.size()]));
        }

        List<String> extent = criteria.getExtent();
        if (!isEmpty(extent)) {
            builder.queryParam("worktime-extent", extent.toArray(new Object[extent.size()]));
        }

        Parttime parttime = criteria.getParttime();
        if (parttime != null && parttime.getMin() != null && parttime.getMax() != null) {
            builder.queryParam("parttime.min", parttime.getMin());
            builder.queryParam("parttime.max", parttime.getMax());
        }

        List<String> drivinglicence = criteria.getDrivinglicence();
        if (!isEmpty(drivinglicence)) {
            builder.queryParam("driving-licence", drivinglicence.toArray(new Object[drivinglicence.size()]));
        }

        List<String> employmenttype = criteria.getEmploymenttype();
        if (!isEmpty(employmenttype)) {
            builder.queryParam("employment-type", employmenttype.toArray(new Object[employmenttype.size()]));
        }

        Boolean experience = criteria.getExperience();
        if (experience != null) {
            builder.queryParam("experience", experience);
        }

        List<String> municipality = criteria.getMunicipality();
        if (!isEmpty(municipality)) {
            builder.queryParam("municipality", municipality.toArray(new Object[municipality.size()]));
        }

        List<String> region = criteria.getRegion();
        if (!isEmpty(region)) {
            builder.queryParam("region", region.toArray(new Object[region.size()]));
        }

        List<String> country = criteria.getCountry();
        if (!isEmpty(country)) {
            builder.queryParam("country", country.toArray(new Object[country.size()]));
        }

        if(!isEmpty(criteria.getGeographicCoordinates())) {
            List<String> radius = new ArrayList<>();
            for (Criteria.GeographicCoordinate coordinate : criteria.getGeographicCoordinates()) {
                if (isNotEmpty(coordinate.getLatitude()) && isNotEmpty(coordinate.getLongitude())) {
                    builder.queryParam("position", coordinate.getLatitude() + "," + coordinate.getLongitude());
                    if (isNotEmpty(coordinate.getRadius())) {
                        radius.add(coordinate.getRadius());
                    }
                }
            }
            if (!isEmpty(radius)) {
                builder.queryParam("position.radius", radius.toArray(new Object[radius.size()]));
            }
        }

        String url = builder.build().encode().toUriString();
        LOGGER.debug("created url: {}", url);

        return Mono.just(url);
    }
}
