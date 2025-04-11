package test;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void createScheduleTest() {
        // given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("유저A", "할일 제목", "할일 내용");
        Schedule savedSchedule = new Schedule(requestDto);

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        // when
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);

        // then
        assertEquals("할일 제목", responseDto.getTitle());
        assertEquals("유저A", responseDto.getUsername());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }
}

