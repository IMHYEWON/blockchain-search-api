-- 테스트용 샘플 데이터
-- 이 파일은 개발 환경에서만 사용됩니다.

-- 네트워크 정보 테이블 (메타데이터용)
CREATE TABLE IF NOT EXISTS networks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    chain_id BIGINT NOT NULL,
    rpc_url VARCHAR(255),
    explorer_url VARCHAR(255),
    currency_symbol VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 지원 네트워크 데이터 삽입
INSERT INTO networks (name, chain_id, rpc_url, explorer_url, currency_symbol) VALUES
('ethereum', 1, 'https://mainnet.infura.io/v3/your-project-id', 'https://etherscan.io', 'ETH'),
('bsc', 56, 'https://bsc-dataseed.binance.org', 'https://bscscan.com', 'BNB'),
('polygon', 137, 'https://polygon-rpc.com', 'https://polygonscan.com', 'MATIC'),
('arbitrum', 42161, 'https://arb1.arbitrum.io/rpc', 'https://arbiscan.io', 'ETH'),
('optimism', 10, 'https://mainnet.optimism.io', 'https://optimistic.etherscan.io', 'ETH');

-- 검색 히스토리 테이블 (선택사항)
CREATE TABLE IF NOT EXISTS search_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    query VARCHAR(255) NOT NULL,
    network VARCHAR(50),
    result_count INT,
    search_time_ms BIGINT,
    user_ip VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성 상태 테이블
CREATE TABLE IF NOT EXISTS index_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    index_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    document_count BIGINT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 상태 초기화
INSERT INTO index_status (index_name, status, document_count) VALUES
('transactions', 'active', 0),
('addresses', 'active', 0),
('tokens', 'active', 0);
