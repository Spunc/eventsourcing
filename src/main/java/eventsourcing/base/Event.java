package eventsourcing.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eventsourcing.Config;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = Config.MESSAGE_CONVERTER_TYPE_ID)
public interface Event {}
