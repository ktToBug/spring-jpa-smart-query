package io.github.kttobug.spring;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

public class LambdaQueryRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID>
        extends JpaRepositoryFactoryBean<R, T, ID> {

    public LambdaQueryRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected JpaRepositoryFactory createRepositoryFactory(EntityManager em) {
        return new LambdaQueryRepositoryFactory(em);
    }

    private static class LambdaQueryRepositoryFactory extends JpaRepositoryFactory {
        private final EntityManager entityManager;

        public LambdaQueryRepositoryFactory(EntityManager em) {
            super(em);
            this.entityManager = em;
        }

        @Override
        protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
            RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

            if (LambdaQueryExecutor.class.isAssignableFrom(metadata.getRepositoryInterface())) {
                LambdaQueryExecutorImpl<?> executor = new LambdaQueryExecutorImpl<>(
                        getEntityInformation(metadata.getDomainType()),
                        entityManager
                );
                fragments = fragments.append(RepositoryFragment.implemented(executor));
            }

            return fragments;
        }
    }
}