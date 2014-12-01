package crusader.multitenant.strategies;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link TenantIdentificationStrategy} that matches the tenant identity
 * from request URL pattern.
 *
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public class PatternURLIdentificationStrategy implements TenantIdentificationStrategy {
    public static final String PARAM_URL_PATTERN = "URL_PATTERN";
    public static final String PARAM_URL_PATTERN_MATCH_POSITION = "URL_PATTERN_MATCH_POSITION";

    private Pattern pattern;
    private int matchPosition;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern must not be null.");
        }
        this.pattern = pattern;
    }

    public int getMatchPosition() {
        return matchPosition;
    }

    public void setMatchPosition(int matchPosition) {
        this.matchPosition = matchPosition;
    }

    @Override
    public Object identifyTenant(HttpServletRequest request) {
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(getMatchPosition());
    }

    @Override
    public void initialize(Map<String, String> params) {
        String pattern = "";

        if (params != null) {
            if (params.containsKey(PARAM_URL_PATTERN)) {
                pattern = params.get(PARAM_URL_PATTERN);
            }
            if (params.containsKey(PARAM_URL_PATTERN_MATCH_POSITION)) {
                setMatchPosition(Integer.parseInt(params.get(PARAM_URL_PATTERN_MATCH_POSITION).trim()));
            }
        }

        setPattern(Pattern.compile(pattern.trim()));
    }
}
