import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  scenarios: {
    load_50vus: {
      executor: "shared-iterations",
      vus: 50,
      iterations: 50,
      startTime: "0s",
    },
  },
};

export default function() {
  const url = 'http://localhost:8080/api/v1/festival/around';

  // 올바른 URL 인코딩 수행
  const addr1 = encodeURIComponent('서울');
  const addr2 = encodeURIComponent('강남구');
  const queryString = `?addr1=${addr1}&addr2=${addr2}`;

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.get(url + queryString, params);

  sleep(3);
}
