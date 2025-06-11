package hr.tvz.trackerplatform.shared.mapper;

import hr.tvz.trackerplatform.daily_check.dto.DailyQuestionDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ModelMapper modelMapper;

    public <T, S> S map(T entity, Class<S> destinationClass) {
        return modelMapper.map(entity, destinationClass);
    }

    public <T, S> List<S> mapList(Collection<T> entityList, Class<S> destinationClass) {
        return entityList.stream().map(entity -> modelMapper.map(entity, destinationClass)).toList();
    }
}
