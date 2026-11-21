package sixseven.client;

import java.util.List;

public record LookupData(
    List list, SmoothScrollState smoothScrollState, StaggeredTransition staggeredTransition) {}
