package com.birdbi.api.security;

import com.birdbi.api.CamelCaseMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {
    @Override
    public CommUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CamelCaseMap map = new CamelCaseMap();
        UserDetails user = User.builder().build();

        return new CommUserDetails(user, map);
    }

    private CommUserDetails createUserDetails(CamelCaseMap map) {
        UserDetails user = User.builder()
                .username((String)map.get("adminId"))
                .password((String)map.get("passwd"))
                .authorities(new SimpleGrantedAuthority("ROLE_" + map.get("role")))
                .build();

        return new CommUserDetails(user, map);
    }

    public CommUserDetails setting(String username) throws Exception{
        int count = 1;
        CamelCaseMap map = null;
        List<CamelCaseMap> settingList = null;

        /*map = adminLoginMapper.getAdminById(username);
        settingList = adminSettingMapper.getSettingList();

        if(settingList.isEmpty()){
            defaultSettingUtil.insertDefault();
            settingList = adminSettingMapper.getSettingList();
        }
        Long sgc103 = CodeEnum.SETTING_GROUP_CODE_1010202.getCode();
        CamelCaseMap sList = adminSettingMapper.selectSetting(sgc103, "1210103");

        Long sgc104 = CodeEnum.SETTING_GROUP_CODE_1010202.getCode();
        CamelCaseMap eList = adminSettingMapper.selectSetting(sgc104, "1210104");


        if(sList == null && eList == null){
            defaultSettingUtil.insertCoreTime();
            settingList = adminSettingMapper.getSettingList();
        }


        for(CamelCaseMap setting : settingList){
            map.put("setting" + count , setting);
            count++;
        }*/
        return createUserDetails(map);
    }
}
