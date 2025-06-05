package hr.tvz.trackerplatform.shared.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ModelMapper modelMapper;

    public <T, S> S map(T entity, Class<S> destinationClass) {
        return modelMapper.map(entity, destinationClass);
    }

    public <T, S> List<S> mapList(List<T> entityList, Class<S> destinationClass) {
        return entityList.stream().map(entity -> modelMapper.map(entity, destinationClass)).toList();
    }

    public <T, S> List<S> mapList(Set<T> entitySet, Class<S> destinationClass) {
        return entitySet.stream().map(entity -> modelMapper.map(entity, destinationClass)).toList();
    }
}
