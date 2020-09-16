package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudioOutEntities extends StudioEntities
{
    private List<String> distribute;
}
