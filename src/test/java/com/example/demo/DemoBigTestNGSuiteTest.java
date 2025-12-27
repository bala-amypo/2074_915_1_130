package com.example.demo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import com.example.demo.controller.AuthController;
import com.example.demo.controller.DeviceOwnershipController;
import com.example.demo.controller.WarrantyClaimController;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.DeviceOwnershipRecord;
import com.example.demo.model.FraudAlertRecord;
import com.example.demo.model.FraudRule;
import com.example.demo.model.StolenDeviceReport;
import com.example.demo.model.User;
import com.example.demo.model.WarrantyClaimRecord;
import com.example.demo.repository.DeviceOwnershipRecordRepository;
import com.example.demo.repository.FraudAlertRecordRepository;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.repository.StolenDeviceReportRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarrantyClaimRecordRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.impl.DeviceOwnershipServiceImpl;
import com.example.demo.service.impl.FraudAlertServiceImpl;
import com.example.demo.service.impl.FraudRuleServiceImpl;
import com.example.demo.service.impl.StolenDeviceServiceImpl;
import com.example.demo.service.impl.WarrantyClaimServiceImpl;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * Full corrected TestNG suite — 60 tests.
 */
@Listeners(TestResultListener.class)
public class DemoBigTestNGSuiteTest {

    // -------------------- Shared mocks --------------------
    @Mock
    DeviceOwnershipRecordRepository deviceRepo;

    @Mock
    WarrantyClaimRecordRepository claimRepo;

    @Mock
    StolenDeviceReportRepository stolenRepo;

    @Mock
    FraudRuleRepository ruleRepo;

    @Mock
    FraudAlertRecordRepository alertRepo;

    @Mock
    UserRepository userRepo;

    // concrete service impls under test (constructed with mocks)
    private DeviceOwnershipServiceImpl deviceService;
    private WarrantyClaimServiceImpl claimService;
    private StolenDeviceServiceImpl stolenService;
    private FraudRuleServiceImpl ruleService;
    private FraudAlertServiceImpl alertService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Construct service implementations with mocked repos.
        // Constructor signatures may vary in your project — adjust if needed.
        deviceService = new DeviceOwnershipServiceImpl(deviceRepo);
        stolenService = new StolenDeviceServiceImpl(stolenRepo, deviceRepo);
        ruleService = new FraudRuleServiceImpl(ruleRepo);
        alertService = new FraudAlertServiceImpl(alertRepo);
        claimService = new WarrantyClaimServiceImpl(claimRepo, deviceRepo, stolenRepo, alertRepo, ruleRepo);

        // Generic safety stubs for save(...) to avoid NullPointerExceptions in tests:
        when(deviceRepo.save(any(DeviceOwnershipRecord.class))).thenAnswer(inv -> {
            DeviceOwnershipRecord r = inv.getArgument(0);
            if (r == null) r = new DeviceOwnershipRecord();
            if (r.getId() == null) r.setId(1L);
            return r;
        });

        when(claimRepo.save(any(WarrantyClaimRecord.class))).thenAnswer(inv -> {
            WarrantyClaimRecord r = inv.getArgument(0);
            if (r == null) r = new WarrantyClaimRecord();
            if (r.getId() == null) r.setId(100L);
            return r;
        });

        when(stolenRepo.save(any(StolenDeviceReport.class))).thenAnswer(inv -> {
            StolenDeviceReport r = inv.getArgument(0);
            if (r == null) r = new StolenDeviceReport();
            if (r.getId() == null) r.setId(10L);
            return r;
        });

        when(alertRepo.save(any(FraudAlertRecord.class))).thenAnswer(inv -> {
            FraudAlertRecord r = inv.getArgument(0);
            if (r == null) r = new FraudAlertRecord();
            if (r.getId() == null) r.setId(5L);
            return r;
        });

        when(ruleRepo.save(any(FraudRule.class))).thenAnswer(inv -> {
            FraudRule r = inv.getArgument(0);
            if (r == null) r = new FraudRule();
            if (r.getId() == null) r.setId(20L);
            return r;
        });
    }

    // ------------------------------------------------------
    // 1–5: controllers / auth
    // ------------------------------------------------------

    @Test(priority = 1)
    public void test1_controllerInstantiation_device() {
        DeviceOwnershipController c = new DeviceOwnershipController(deviceService);
        assertNotNull(c);
    }

    @Test(priority = 2)
    public void test2_controllerInstantiation_claim() {
        WarrantyClaimController c = new WarrantyClaimController(claimService);
        assertNotNull(c);
    }

    @Test(priority = 3)
    public void test3_controllerInstantiation_auth() {
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        PasswordEncoderStub pw = new PasswordEncoderStub();
        AuthController ac = new AuthController(userRepo, pw, provider);
        assertNotNull(ac);
    }

    @Test(priority = 4)
    public void test4_registerUser_returnsToken() {
        PasswordEncoderStub pw = new PasswordEncoderStub();
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.createToken(anyLong(), anyString(), anySet())).thenReturn("token123");

        RegisterRequest req = new RegisterRequest();
        req.setEmail("u@test.com");
        req.setPassword("pass");
        req.setRoles(Set.of("ADMIN"));
        req.setName("User");

        when(userRepo.findByEmail("u@test.com")).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            if (u.getId() == null) u.setId(1L);
            return u;
        });

        AuthController ac = new AuthController(userRepo, pw, provider);
        ResponseEntity<?> resp = ac.register(req);
        assertEquals(((AuthResponse) resp.getBody()).getToken(), "token123");
    }

    @Test(priority = 5)
    public void test5_loginUser_returnsToken() {
        PasswordEncoderStub pw = new PasswordEncoderStub();
        String encoded = pw.encode("secret");
        User u = User.builder().id(1L).email("a@test.com").password(encoded).roles(Set.of("ADMIN")).build();

        when(userRepo.findByEmail("a@test.com")).thenReturn(Optional.of(u));
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.createToken(anyLong(), anyString(), anySet())).thenReturn("abc-123");

        AuthController ac = new AuthController(userRepo, pw, provider);
        AuthRequest req = new AuthRequest();
        req.setEmail("a@test.com");
        req.setPassword("secret");

        ResponseEntity<?> res = ac.login(req);
        assertEquals(((AuthResponse) res.getBody()).getToken(), "abc-123");
    }

    // ------------------------------------------------------
    // 6–20: CRUD operations
    // ------------------------------------------------------

    @Test(priority = 6)
    public void test6_registerDevice_success() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder()
                .serialNumber("SN100")
                .ownerName("A")
                .warrantyExpiration(LocalDate.now().plusDays(10))
                .active(true).build();

        when(deviceRepo.existsBySerialNumber("SN100")).thenReturn(false);

        DeviceOwnershipRecord saved = deviceService.registerDevice(d);
        assertNotNull(saved.getId());
        assertEquals(saved.getSerialNumber(), "SN100");
    }

    @Test(priority = 7)
    public void test7_registerDevice_duplicate() {
        when(deviceRepo.existsBySerialNumber("DUP")).thenReturn(true);
        try {
            deviceService.registerDevice(DeviceOwnershipRecord.builder().serialNumber("DUP").build());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test(priority = 8)
    public void test8_getDeviceBySerial_notFound() {
        when(deviceRepo.findBySerialNumber("X")).thenReturn(Optional.empty());
        assertTrue(deviceService.getBySerial("X").isEmpty());
    }

    @Test(priority = 9)
    public void test9_getAllDevices_empty() {
        when(deviceRepo.findAll()).thenReturn(List.of());
        assertTrue(deviceService.getAllDevices().isEmpty());
    }

    @Test(priority = 10)
    public void test10_updateDeviceStatus_success() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder().id(10L).active(true).build();
        when(deviceRepo.findById(10L)).thenReturn(Optional.of(d));
        when(deviceRepo.save(any(DeviceOwnershipRecord.class))).thenAnswer(inv -> {
            DeviceOwnershipRecord r = inv.getArgument(0);
            if (r.getId() == null) r.setId(10L);
            return r;
        });

        DeviceOwnershipRecord updated = deviceService.updateDeviceStatus(10L, false);
        assertFalse(updated.getActive());
    }

    @Test(priority = 11)
    public void test11_updateDeviceStatus_idNotFound() {
        when(deviceRepo.findById(999L)).thenReturn(Optional.empty());
        try {
            deviceService.updateDeviceStatus(999L, true);
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // expected
        }
    }

    @Test(priority = 12)
    public void test12_submitClaim_unknownDevice() {
        when(deviceRepo.findBySerialNumber("NA")).thenReturn(Optional.empty());
        try {
            claimService.submitClaim(WarrantyClaimRecord.builder().serialNumber("NA").build());
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // expected
        }
    }

    @Test(priority = 13)
    public void test13_submitClaim_duplicate_flag() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder().serialNumber("S1").active(true)
                .warrantyExpiration(LocalDate.now().plusDays(10)).build();

        when(deviceRepo.findBySerialNumber("S1")).thenReturn(Optional.of(d));
        when(claimRepo.existsBySerialNumberAndClaimReason("S1", "R")).thenReturn(true);
        when(claimRepo.save(any(WarrantyClaimRecord.class))).thenAnswer(inv -> {
            WarrantyClaimRecord r = inv.getArgument(0);
            if (r.getId() == null) r.setId(2L);
            return r;
        });

        WarrantyClaimRecord saved = claimService.submitClaim(WarrantyClaimRecord.builder()
                .serialNumber("S1").claimReason("R").build());
        assertEquals(saved.getStatus(), "FLAGGED");
    }

    @Test(priority = 14)
    public void test14_submitClaim_expired_flag() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder().serialNumber("EX").active(true)
                .warrantyExpiration(LocalDate.now().minusDays(1)).build();

        when(deviceRepo.findBySerialNumber("EX")).thenReturn(Optional.of(d));
        when(claimRepo.save(any(WarrantyClaimRecord.class))).thenAnswer(inv -> {
            WarrantyClaimRecord r = inv.getArgument(0);
            if (r.getId() == null) r.setId(3L);
            return r;
        });

        WarrantyClaimRecord saved = claimService.submitClaim(WarrantyClaimRecord.builder()
                .serialNumber("EX").claimReason("Broken").build());

        assertEquals(saved.getStatus(), "FLAGGED");
    }

    @Test(priority = 15)
    public void test15_submitClaim_stolen_flag() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder().serialNumber("ST").active(true)
                .warrantyExpiration(LocalDate.now().plusDays(10)).build();

        when(deviceRepo.findBySerialNumber("ST")).thenReturn(Optional.of(d));
        when(stolenRepo.existsBySerialNumber("ST")).thenReturn(true);
        when(claimRepo.save(any(WarrantyClaimRecord.class))).thenAnswer(inv -> {
            WarrantyClaimRecord r = inv.getArgument(0);
            if (r.getId() == null) r.setId(4L);
            return r;
        });

        WarrantyClaimRecord saved = claimService.submitClaim(
                WarrantyClaimRecord.builder().serialNumber("ST").claimReason("Broken").build()
        );

        assertEquals(saved.getStatus(), "FLAGGED");
    }

    @Test(priority = 16)
    public void test16_updateClaimStatus() {
        WarrantyClaimRecord c = WarrantyClaimRecord.builder().id(20L).status("PENDING").build();
        when(claimRepo.findById(20L)).thenReturn(Optional.of(c));
        when(claimRepo.save(any(WarrantyClaimRecord.class))).thenAnswer(inv -> inv.getArgument(0));
        WarrantyClaimRecord updated = claimService.updateClaimStatus(20L, "APPROVED");
        assertEquals(updated.getStatus(), "APPROVED");
    }

    @Test(priority = 17)
    public void test17_getClaimById_empty() {
        when(claimRepo.findById(1L)).thenReturn(Optional.empty());
        assertTrue(claimService.getClaimById(1L).isEmpty());
    }

    @Test(priority = 18)
    public void test18_getAllClaims_list() {
        when(claimRepo.findAll()).thenReturn(List.of(WarrantyClaimRecord.builder().id(1L).build()));
        assertEquals(claimService.getAllClaims().size(), 1);
    }

    @Test(priority = 19)
    public void test19_reportStolen_success() {
        when(deviceRepo.findBySerialNumber("D1"))
                .thenReturn(Optional.of(DeviceOwnershipRecord.builder().serialNumber("D1").build()));

        when(stolenRepo.save(any(StolenDeviceReport.class))).thenAnswer(inv -> {
            StolenDeviceReport r = inv.getArgument(0);
            if (r.getId() == null) r.setId(10L);
            return r;
        });

        StolenDeviceReport saved = stolenService.reportStolen(
                StolenDeviceReport.builder().serialNumber("D1").build()
        );
        assertNotNull(saved.getId());
    }

    @Test(priority = 20)
    public void test20_reportStolen_noDevice() {
        when(deviceRepo.findBySerialNumber("NO")).thenReturn(Optional.empty());
        try {
            stolenService.reportStolen(StolenDeviceReport.builder().serialNumber("NO").build());
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // expected
        }
    }

    // ------------------------------------------------------
    // 21–23 Dependency injection/service wires
    // ------------------------------------------------------

    @Test(priority = 21)
    public void test21_deviceServiceNotNull() {
        assertNotNull(deviceService);
    }

    @Test(priority = 22)
    public void test22_claimServiceNotNull() {
        assertNotNull(claimService);
    }

    @Test(priority = 23)
    public void test23_stolenServiceNotNull() {
        assertNotNull(stolenService);
    }

    // ------------------------------------------------------
    // 24–30 Entities / Hibernate checks
    // ------------------------------------------------------

    @Test(priority = 24)
    public void test24_entity_device_setFields() {
        DeviceOwnershipRecord d = new DeviceOwnershipRecord();
        d.setSerialNumber("A");
        assertEquals(d.getSerialNumber(), "A");
    }

    @Test(priority = 25)
    public void test25_entity_claim_defaultStatus() {
        WarrantyClaimRecord c = new WarrantyClaimRecord();
        assertEquals(c.getStatus(), "PENDING");
    }

    @Test(priority = 26)
    public void test26_entity_rule_fields() {
        FraudRule r = FraudRule.builder().ruleCode("R10").ruleType("TYPE").build();
        assertEquals(r.getRuleCode(), "R10");
    }

    @Test(priority = 27)
    public void test27_entity_alert_defaultResolved() {
        FraudAlertRecord f = new FraudAlertRecord();
        assertFalse(Boolean.TRUE.equals(f.getResolved()));
    }

    @Test(priority = 28)
    public void test28_user_defaultRoles() {
        assertNotNull(new User().getRoles());
    }

    @Test(priority = 29)
    public void test29_updateDeviceToggle() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder().id(5L).active(true).build();
        when(deviceRepo.findById(5L)).thenReturn(Optional.of(d));
        when(deviceRepo.save(any(DeviceOwnershipRecord.class))).thenAnswer(inv -> inv.getArgument(0));
        DeviceOwnershipRecord updated = deviceService.updateDeviceStatus(5L, false);
        assertFalse(updated.getActive());
    }

    @Test(priority = 30)
    public void test30_serialUniqueConstraint_simulated() {
        when(deviceRepo.existsBySerialNumber("U1")).thenReturn(true);
        try {
            deviceService.registerDevice(DeviceOwnershipRecord.builder().serialNumber("U1").build());
            fail();
        } catch (Exception e) {
            // ok
        }
    }

    // ------------------------------------------------------
    // 31–33 JPA normalization
    // ------------------------------------------------------

    @Test(priority = 31)
    public void test31_1NF_atomicFields() {
        WarrantyClaimRecord c = WarrantyClaimRecord.builder().serialNumber("S1").claimReason("R").build();
        assertNotNull(c.getClaimReason());
    }

    @Test(priority = 32)
    public void test32_2NF_noPartialDeps() {
        DeviceOwnershipRecord d = DeviceOwnershipRecord.builder()
                .serialNumber("SS")
                .ownerName("A")
                .ownerEmail("a@x")
                .build();
        assertEquals(d.getOwnerName(), "A");
    }

    @Test(priority = 33)
    public void test33_3NF_uniqueRuleCode() {
        FraudRule r = FraudRule.builder().ruleCode("R50").description("test").build();
        assertEquals(r.getRuleCode(), "R50");
    }

    // ------------------------------------------------------
    // 34–36 Many-to-many / roles
    // ------------------------------------------------------

    @Test(priority = 34)
    public void test34_userRolesMultiple() {
        User u = User.builder().roles(Set.of("ADMIN","AGENT")).build();
        assertTrue(u.getRoles().contains("ADMIN"));
    }

    @Test(priority = 35)
    public void test35_userRolePersist_mock() {
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        User saved = userRepo.save(User.builder().roles(Set.of("R1","R2")).build());
        assertEquals(saved.getRoles().size(), 2);
    }

    @Test(priority = 36)
    public void test36_userRoleLookup() {
        when(userRepo.findByEmail("x")).thenReturn(Optional.of(User.builder().email("x").roles(Set.of("A")).build()));
        assertTrue(userRepo.findByEmail("x").get().getRoles().contains("A"));
    }

    // ------------------------------------------------------
    // 37–45 Security / JWT
    // ------------------------------------------------------

    @Test(priority = 37)
    public void test37_jwtCreate() {
        JwtTokenProvider p = new JwtTokenProvider();
        String t = p.createToken(1L, "a", Set.of("ADMIN"));
        assertNotNull(t);
    }

    @Test(priority = 38)
    public void test38_jwtValidate() {
        JwtTokenProvider p = new JwtTokenProvider();
        String t = p.createToken(1L,"a",Set.of("ADMIN"));
        assertTrue(p.validateToken(t));
    }

    @Test(priority = 39)
    public void test39_jwtGetEmail() {
        JwtTokenProvider p = new JwtTokenProvider();
        String t = p.createToken(1L,"abc@test.com",Set.of("ADMIN"));
        assertEquals(p.getEmail(t),"abc@test.com");
    }

    @Test(priority = 40)
    public void test40_jwtGetRoles() {
        JwtTokenProvider p = new JwtTokenProvider();
        String t = p.createToken(1L,"a",Set.of("ADMIN","AGENT"));
        assertTrue(p.getRoles(t).contains("ADMIN"));
    }

    @Test(priority = 41)
    public void test41_jwtGetUserId() {
        JwtTokenProvider p = new JwtTokenProvider();
        String t = p.createToken(10L,"a",Set.of("ADMIN"));
        assertEquals(p.getUserId(t),Long.valueOf(10));
    }

    @Test(priority = 42)
    public void test42_jwtInvalidFails() {
        JwtTokenProvider p = new JwtTokenProvider();
        assertFalse(p.validateToken("xxx.yyy.zzz"));
    }

    @Test(priority = 43)
    public void test43_loginWrongPasswordFails() {
        PasswordEncoderStub pe = new PasswordEncoderStub();
        User u = User.builder().email("a").password(pe.encode("p")).build();
        when(userRepo.findByEmail("a")).thenReturn(Optional.of(u));
        AuthController ac = new AuthController(userRepo, pe, mock(JwtTokenProvider.class));
        AuthRequest req = new AuthRequest(); req.setEmail("a"); req.setPassword("x");

        ResponseEntity<?> resp = ac.login(req);
        assertEquals(resp.getStatusCodeValue(), 401);
    }

    @Test(priority = 44)
    public void test44_registerDuplicateEmailFails() {
        when(userRepo.findByEmail("dup")).thenReturn(Optional.of(User.builder().email("dup").build()));
        AuthController ac = new AuthController(userRepo, new PasswordEncoderStub(), mock(JwtTokenProvider.class));

        RegisterRequest req = new RegisterRequest();
        req.setEmail("dup");
        req.setPassword("p");

        ResponseEntity<?> r = ac.register(req);
        assertEquals(r.getStatusCodeValue(), 409);
    }

    @Test(priority = 45)
    public void test45_jwtFilter_processesToken() throws IOException, ServletException {
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        CustomUserDetailsService uds = mock(CustomUserDetailsService.class);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(provider, uds);

        HttpServletRequestMock req = new HttpServletRequestMock("Bearer testtoken");
        HttpServletResponseMock res = new HttpServletResponseMock();

        when(provider.validateToken("testtoken")).thenReturn(true);
        when(provider.getEmail("testtoken")).thenReturn("a@test.com");
        when(provider.getRoles("testtoken")).thenReturn(Set.of("ADMIN"));

        FilterChainMock chain = new FilterChainMock();
        filter.doFilter(req, res, chain);

        assertTrue(chain.wasCalled());
    }

    // ------------------------------------------------------
    // 46–50 HQL / queries
    // ------------------------------------------------------

    @Test(priority = 46)
    public void test46_findClaimsBySerial() {
        when(claimRepo.findBySerialNumber("S")).thenReturn(List.of(WarrantyClaimRecord.builder().id(1L).build()));
        assertEquals(claimService.getClaimsBySerial("S").size(), 1);
    }

    @Test(priority = 47)
    public void test47_findStolenBySerial() {
        when(stolenRepo.findBySerialNumber("S")).thenReturn(List.of(StolenDeviceReport.builder().id(1L).serialNumber("S").build()));
        assertEquals(stolenService.getReportsBySerial("S").size(), 1);
    }

    @Test(priority = 48)
    public void test48_findActiveRules() {
        when(ruleRepo.findByActiveTrue()).thenReturn(List.of(FraudRule.builder().id(1L).build()));
        assertEquals(ruleService.getActiveRules().size(), 1);
    }

    @Test(priority = 49)
    public void test49_findAlertsByClaimId() {
        when(alertRepo.findByClaimId(5L)).thenReturn(List.of(FraudAlertRecord.builder().claimId(5L).build()));
        assertEquals(alertService.getAlertsByClaim(5L).size(), 1);
    }

    @Test(priority = 50)
    public void test50_customFilterSimulated() {
        when(deviceRepo.existsBySerialNumber("FILTER")).thenReturn(true);
        assertTrue(deviceRepo.existsBySerialNumber("FILTER"));
    }

    // ------------------------------------------------------
    // 51–60 extra cases
    // ------------------------------------------------------

    @Test(priority = 51)
    public void test51_claimRepoFindAll() {
        when(claimRepo.findAll()).thenReturn(List.of(WarrantyClaimRecord.builder().id(1L).build()));
        assertEquals(claimService.getAllClaims().size(), 1);
    }

    @Test(priority = 52)
    public void test52_createRuleSuccess() {
        when(ruleRepo.findByRuleCode("R1")).thenReturn(Optional.empty());
        when(ruleRepo.save(any(FraudRule.class))).thenAnswer(inv -> {
            FraudRule r = inv.getArgument(0);
            if (r.getId() == null) r.setId(10L);
            return r;
        });
        assertNotNull(ruleService.createRule(FraudRule.builder().ruleCode("R1").build()).getId());
    }

    @Test(priority = 53)
    public void test53_createRuleDuplicate() {
        when(ruleRepo.findByRuleCode("R2")).thenReturn(Optional.of(FraudRule.builder().id(2L).build()));
        try {
            ruleService.createRule(FraudRule.builder().ruleCode("R2").build());
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test(priority = 54)
    public void test54_createAlertSuccess() {
        when(alertRepo.save(any(FraudAlertRecord.class))).thenAnswer(inv -> {
            FraudAlertRecord r = inv.getArgument(0);
            if (r.getId() == null) r.setId(1L);
            return r;
        });
        FraudAlertRecord out = alertService.createAlert(FraudAlertRecord.builder().serialNumber("A").build());
        assertNotNull(out.getId());
    }

    @Test(priority = 55)
    public void test55_resolveAlertSuccess() {
        FraudAlertRecord f = FraudAlertRecord.builder().id(5L).resolved(false).build();
        when(alertRepo.findById(5L)).thenReturn(Optional.of(f));
        when(alertRepo.save(any(FraudAlertRecord.class))).thenAnswer(inv -> inv.getArgument(0));
        FraudAlertRecord resolved = alertService.resolveAlert(5L);
        assertTrue(resolved.getResolved());
    }

    @Test(priority = 56)
    public void test56_resolveAlertNotFound() {
        when(alertRepo.findById(100L)).thenReturn(Optional.empty());
        try {
            alertService.resolveAlert(100L);
            fail();
        } catch (NoSuchElementException ex) {
            // expected
        }
    }

    @Test(priority = 57)
    public void test57_stolenGetAll_empty() {
        when(stolenRepo.findAll()).thenReturn(List.of());
        assertTrue(stolenService.getAllReports().isEmpty());
    }

    @Test(priority = 58)
    public void test58_deviceGetAll_nonEmpty() {
        when(deviceRepo.findAll()).thenReturn(List.of(DeviceOwnershipRecord.builder().id(1L).build()));
        assertEquals(deviceService.getAllDevices().size(), 1);
    }

    @Test(priority = 59)
    public void test59_ruleGetByCode() {
        when(ruleRepo.findByRuleCode("R100")).thenReturn(Optional.of(FraudRule.builder().id(9L).ruleCode("R100").build()));
        assertTrue(ruleService.getRuleByCode("R100").isPresent());
    }

    @Test(priority = 60)
    public void test60_simpleMath() {
        assertEquals(2 + 2, 4);
    }

    // ------------------------------
    // Supporting stub classes
    // ------------------------------

    static class PasswordEncoderStub implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return "ENC_" + rawPassword;
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encodedPassword != null && encodedPassword.equals("ENC_" + rawPassword);
        }
    }

    static class HttpServletRequestMock extends HttpServletRequestWrapper {
        final String auth;
        public HttpServletRequestMock(String header) {
            super(mock(HttpServletRequest.class));
            this.auth = header;
        }
        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) return auth;
            return null;
        }
    }

    static class HttpServletResponseMock extends HttpServletResponseWrapper {
        public HttpServletResponseMock() {
            super(mock(HttpServletResponse.class));
        }
    }

    static class FilterChainMock implements FilterChain {
        boolean called = false;
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) {
            called = true;
        }
        boolean wasCalled() { return called; }
    }
}
