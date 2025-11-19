package com.bomi.main.service;

import com.bomi.main.DTO.RequestPostWardLocation;
import com.bomi.main.DTO.WardLocationDetail;
import com.bomi.main.DTO.WardsLocationDTO;
import com.bomi.main.entity.Care;
import com.bomi.main.entity.Member;
import com.bomi.main.entity.MemberLocation;
import com.bomi.main.repository.CareRepository;
import com.bomi.main.repository.MemberLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberLocationService {

    private final CareRepository careRepository;
    private final MemberLocationRepository memberLocationRepository;

    public WardsLocationDTO getWardsLocation(Long guardianId) {
        Set<Care> wards = careRepository.findWardsByGuardianId(guardianId);


        Map<Long, Member> wardsMap = wards.stream()
                .map(Care::getWard)
                .collect(Collectors.toMap(
                        Member::getMemberId,
                        ward -> ward
                ));

        List<Long> wardIds = wardsMap.keySet().stream().toList();

        List<MemberLocation> latestLocations = memberLocationRepository.findLatestLocationsByMemberIds(wardIds);

        List<WardLocationDetail> details = latestLocations.stream()
                .map(location -> {
                    Member ward = wardsMap.get(location.getMemberId());
                    String wardDisplayName = (ward != null) ? ward.getMemberDisplayName() : "Unknown"; // 이름이 없을 경우 대비

                    return new WardLocationDetail(
                            location.getMemberId(),
                            wardDisplayName,
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getTimestamp()
                    );
                })
                .toList();

        return new WardsLocationDTO(details);
    }

    public void postWardLocation(Long wardId, RequestPostWardLocation requestPostWardLocation) {
        LocalDateTime now = LocalDateTime.now();

        MemberLocation memberLocation = MemberLocation.builder()
                .memberId(wardId)
                .latitude(requestPostWardLocation.latitude())
                .longitude(requestPostWardLocation.longitude())
                .timestamp(now)
                .build();
        memberLocationRepository.save(memberLocation);
    }
}
