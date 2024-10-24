package com.lu.gademo.entity.ga;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneInfo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "sceneId", nullable = false)
    private Long sceneId;

//    @Column(name = "sceneName")
    private String sceneName;

//    @Column(name = "sceneChineseName")
    private String sceneChineseName;

//    @Column(name = "fieldNumber")
    private Integer fieldNumber;

}