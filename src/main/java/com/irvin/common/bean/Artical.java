package com.irvin.common.bean;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author irvin
 * @date Create in 上午1:48 2017/8/5
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Artical implements Serializable {

    private static final long serialVersionUID = -5560135572832498230L;
    private String docNo;
    private String briefTitle;
    private String condition;
    private String title;
    private String content;
    /*private List<String> meshTerms = new ArrayList<>();*/
    private String interventionType;
    private String interventionName;
    private String description;
    private String armGroupLabel;
    /*private List<String> textblocks = new ArrayList<>();*/
    private String gender;
    private String minimumAge;
    private String maximumAge;
    private String healthyVolunteers;
}
